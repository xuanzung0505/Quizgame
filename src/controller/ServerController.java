/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.*;

/**
 *
 * @author typpo
 */
public class ServerController {

//    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket socket;

    private final int port = 8888;

    public ServerController() {
        openServer(port);
        listening();
    }

    private void openServer(int port) {
        try {
            socket = new ServerSocket(port);
            System.out.println("Sever is running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listening() {
        while (!socket.isClosed()) {

            try {
                Socket Serversocket = socket.accept();
                ClientHandler clientHandler = new ClientHandler(Serversocket);
                System.out.println(clientHandler.getPlayer().getUsername() + " has entered the system!");
                System.out.println("Number of clients: " + ClientHandler.clientHandlers.size());

                clientHandler.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //ClientHandler
    public static class ClientHandler extends Thread implements Serializable {

        private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        private Player player;
        private transient Socket socket;
        private transient ObjectOutputStream oos;
        private transient ObjectInputStream ois;
        private transient Connection con;

        private boolean requestStart;
        private volatile boolean requestFinish;
        private ArrayList<Object> matchResult;
        private boolean lock; //lock = true -> save to DB, else the other clienthandler saves
        private transient final Object lockk;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.requestStart = false;
            this.requestFinish = false;
            this.matchResult = null;
            this.lock = false;
            this.lockk = new Object();

            clientHandlers.add(this);

            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                //read Client
                Object obj = ois.readObject();
                if (obj instanceof Message) {
                    Message msg = (Message) obj;
                    if (msg.getMessage().equals("Client")) {
                        this.player = (Player) msg.getData();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            listenForMessage();
//            System.out.println("closed");
        }

        public void listenForMessage() {
//        System.out.println(ClientHandler.getClientHandlers().size());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (socket.isConnected()) {
                        try {
                            Object o = ois.readObject();
                            if (o instanceof Message) {
                                Message msgReceived = (Message) o;
                                System.out.println(msgReceived.getMessage()); //print Message
                                sendMessage(msgReceived);
                            }
                        } catch (Exception e) {
                            closeEverything(socket, ois, oos);
                        }
                    }
                }
            }).start();
        }

        private void RequestOnlineClient(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RequestOnlineClient")) {
                System.out.println(player.isPlaying());
                ArrayList<ClientHandler> clientHandlers_clone = new ArrayList<>();
                for (ClientHandler clientHandler : clientHandlers) {
                    clientHandlers_clone.add(clientHandler);
                }
                Message msgSend = new Message("OnlineClient", clientHandlers_clone);
                System.out.println("OnlineClient: " + clientHandlers_clone.size()); //clone the static clientHandlers because it cant be serialized
                oos.writeObject(msgSend);
                oos.flush();
            }
        }

        private void RequestInvite(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RequestInvite")) { //send to a clientHandler
                Player playerInvite = this.getPlayer(); //player who sends invite
                Player Invitedplayer = (Player) msgReceived.getData(); //player who receives invite

                for (ClientHandler clientHandler : clientHandlers) {
                    String player = clientHandler.getPlayer().getUsername();
                    if (player.equals(Invitedplayer.getUsername())) {
                        Message msg = new Message("Invite", playerInvite);
                        clientHandler.sendMessage(msg);
                        break;
                    }
                }
            }
            if (msgReceived.getMessage().equals("Invite")) { //send to the corresponding client
                oos.writeObject(msgReceived);
                oos.flush();
            }
        }

        private void RespondRefuse(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RespondRefuse")) { //send to a clientHandler
                Player playerRefuse = this.getPlayer(); //player who refuses
                Player playerInvite = (Player) msgReceived.getData(); //player who sends invite

                for (ClientHandler clientHandler : clientHandlers) {
                    String player = clientHandler.getPlayer().getUsername();
                    if (player.equals(playerInvite.getUsername())) {
                        Message msg = new Message("Refuse", playerRefuse);
                        clientHandler.sendMessage(msg);
                        break;
                    }
                }
            }
            if (msgReceived.getMessage().equals("Refuse")) { //send to the corresponding client
                oos.writeObject(msgReceived);
                oos.flush();
            }
        }

        private void RespondAccept(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RespondAccept")) { //send to a clientHandler
                Player playerAccept = this.getPlayer(); //player who accepts
                Player playerInvite = (Player) msgReceived.getData(); //player who sends invite

                for (ClientHandler clientHandler : clientHandlers) {
                    String player = clientHandler.getPlayer().getUsername();
                    if (player.equals(playerInvite.getUsername())) {
                        Message msg = new Message("Accept", playerAccept);
                        clientHandler.sendMessage(msg);
                        break;
                    }
                }
            }
            if (msgReceived.getMessage().equals("Accept")) { //send to the corresponding client
                oos.writeObject(msgReceived);
                oos.flush();
            }
        }

        private void RequestQuestion(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RequestQuestion")) {
                getDBConnection("quizgame", "root", "root");
                ArrayList<Question> questions = loadQuestion();
                Message msg = new Message("Question", questions);
                oos.writeObject(msg);
                oos.flush();
            }
        }

        private void RequestStartGame(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RequestStartGame")) {
                Player opponent = (Player) msgReceived.getData();
//                    System.out.println(opponent);
                this.player.setPlaying(true);
                this.requestStart = true;
                for (ClientHandler clientHandler : clientHandlers) {
                    String player = clientHandler.getPlayer().getUsername();
                    if (player.equals(opponent.getUsername())) {
//                            System.out.println("opponent is ready: "+clientHandler.isRequestStart());
                        while (!clientHandler.isRequestStart()) {
                            //do nothing
//                                System.out.println("waiting...");
                        }
                        Message msg = new Message("StartGame");
                        clientHandler.sendMessage(msg);
                        this.sendMessage(msg);
                        break;
                    }
                }
            }
            if (msgReceived.getMessage().equals("StartGame")) {
                oos.writeObject(msgReceived);
                oos.flush();
            }
        }

        private void RequestExitGame(Message msgReceived) throws IOException {
            if (msgReceived.getMessage().equals("RequestExitGame")) {
                Player opponent = (Player) msgReceived.getData();
                this.player.setPlaying(false);
                this.requestStart = false;

                for (ClientHandler clientHandler : clientHandlers) {
                    String player = clientHandler.getPlayer().getUsername();
                    if (player.equals(opponent.getUsername())) {
                        clientHandler.getPlayer().setPlaying(false);
                        clientHandler.setRequestStart(false);
                        Message msg = new Message("ExitGame");
                        clientHandler.sendMessage(msg);
                        break;
                    }
                }
            }
            if (msgReceived.getMessage().equals("ExitGame")) {
                this.player.setPlaying(false);
                this.requestStart = false;
                oos.writeObject(msgReceived);
                oos.flush();
            }
        }

        private void sendMessage(Message msgReceived) {
            try {
                RequestOnlineClient(msgReceived);
                if (msgReceived.getMessage().equals("RemoveOnlineClient")) {
                    closeEverything(socket, ois, oos);
                }
                RequestInvite(msgReceived);
                RespondRefuse(msgReceived);
                RespondAccept(msgReceived);
                RequestQuestion(msgReceived);
                RequestStartGame(msgReceived);
                RequestExitGame(msgReceived);

                if (msgReceived.getMessage().equals("RequestFinishGame")) {
//                    requestFinish = true;
//                    matchResult = (ArrayList<Object>) msgReceived.getData();
//
//                    Player opponent = (Player) matchResult.get(3);
//                    for (ClientHandler clientHandler : clientHandlers) {
//                        String player = clientHandler.getPlayer().getUsername();
//                        if (player.equals(opponent.getUsername())) {
//                            while (!clientHandler.isRequestFinish()) {
//                                //do nothing
////                                System.out.println("waiting...");
//                            }
//                            //lock = true
////                            synchronized (lockk) {
////                                if (!lock) {
////                                    lock = true;
////                                    System.out.println(this.getPlayer().getUsername());
////
////                                    //clientHandler has data
////                                    ArrayList<Object> opponentResult = (ArrayList<Object>) clientHandler.getMatchResult();
////                                    int correctAns = (int) matchResult.get(0);
////                                    int totalQues = (int) matchResult.get(1);
////                                    long timeLapsed = (long) matchResult.get(2);
////
////                                    int op_correctAns = (int) opponentResult.get(0);
////                                    int op_totalQues = (int) opponentResult.get(1);
////                                    long op_timeLapsed = (long) opponentResult.get(2);
////
////                                    int UserId1 = this.player.getId();
////                                    int UserId2 = opponent.getId();
////                                    String Draw = "false";
////                                    int idWin = 0;
////                                    //dung het va nhanh hon -> win
////                                    //ca 2 co it nhat 1 cau sai -> draw
////                                    if (correctAns == totalQues && op_correctAns == op_totalQues) {
////                                        if (timeLapsed > op_timeLapsed) {
////                                            idWin = UserId1;
////                                        } else if (timeLapsed < op_timeLapsed) {
////                                            idWin = UserId2;
////                                        } else {
////                                            Draw = "true";
////                                        }
////
////                                    } else {
////                                        if (correctAns == totalQues) {
////                                            idWin = UserId1;
////                                        } else if (op_correctAns == totalQues) {
////                                            idWin = UserId2;
////                                        } else {
////                                            Draw = "true";
////                                        }
////                                    }
////
////                                    //save to DB
////                                }
////                                clientHandler.setLock(false);
////                            }
//
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void getDBConnection(String dbName, String username, String password) {
            String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=CONVERT_TO_NULL";
            try {
                con = DriverManager.getConnection(dbUrl, username, password);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ArrayList<Question> loadQuestion() {
            ArrayList<Question> result = new ArrayList<>();
            String sql = "select * from question";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestion(rs.getString("Question"));
                    question.setOptions(new String[]{rs.getString("AnswerA"), rs.getString("AnswerB"),
                        rs.getString("AnswerC"), rs.getString("AnswerD")});
                    question.setAnswer(rs.getString("RightAns"));
//                System.out.println(question.toString());
                    result.add(question);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public void removeClientHandler() {
            clientHandlers.remove(this);
        }

        public void closeEverything(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
//            System.out.println(player.getUsername() + " has left the system!");
            removeClientHandler();
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Player getPlayer() {
            return player;
        }

        public static ArrayList<ClientHandler> getClientHandlers() {
            return clientHandlers;
        }

        public boolean isRequestStart() {
            return requestStart;
        }

        public void setRequestStart(boolean requestStart) {
            this.requestStart = requestStart;
        }

        public boolean isRequestFinish() {
            return requestFinish;
        }

        public void setRequestFinish(boolean requestFinish) {
            this.requestFinish = requestFinish;
        }

        public ArrayList<Object> getMatchResult() {
            return matchResult;
        }

        public boolean isLock() {
            return lock;
        }

        public void setLock(boolean lock) {
            this.lock = lock;
        }

    }
}
