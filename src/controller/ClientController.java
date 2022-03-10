/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.ServerController.ClientHandler;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import model.*;
import view.*;

/**
 *
 * @author typpo
 */
public class ClientController extends Thread {

    private Player player;
    private transient MainFrm mainFrm;
    private transient QuizFrm quizFrm;
    private transient ScoreboardController scoreboardController;
    private transient ScoreboardFrm scoreboardFrm;
    private Player opponent;
    private Player currentPlayer;
    private transient final Object lock;

    private transient Socket socket;
    private transient ObjectOutputStream oos;
    private transient ObjectInputStream ois;

    private final String host = "localhost";
    private final int port = 8888;

    public ClientController(Player player, Socket socket) {
        this.player = player;
        this.socket = socket;
        this.currentPlayer = null;
        this.lock = new Object();

        mainFrm = new MainFrm();

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        init();
    }

    private void init() {
        mainFrm.setjLabel5(player.getUsername());
//        mainFrm.addWindowClosingListener(new windowClosingListener());
        mainFrm.addUpdateListener(new UpdateListener());
        mainFrm.addTableListener(new TableListener());
        mainFrm.addChallengeListener(new ChallengeListener());
        mainFrm.addScoreBoardListener(new ScoreBoardListener());
        mainFrm.setVisible(true);

        Message msg = new Message("Client", player);
        sendMessage(msg);
    }

    private void sendMessage(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
//            closeEverything(mySocket, oos, ois);
        }
    }

    private void listening() {
        while (!socket.isClosed()) {
            try {
                Object o = ois.readObject();
                if (o instanceof Message) {
                    Message msgReceived = (Message) o;
                    System.out.println(msgReceived.getMessage());

                    if (msgReceived.getMessage().equals("OnlineClient")) { //
                        ArrayList<ClientHandler> clientHandlers;
                        clientHandlers = (ArrayList<ClientHandler>) msgReceived.getData();
//                        System.out.println(clientHandlers.size());
                        mainFrm.setjTable1(clientHandlers);
                    }

                    if (msgReceived.getMessage().equals("Invite")) { //receive invite
                        Player playerInvite = (Player) msgReceived.getData(); //player who sends invite

                        if (player.isPlaying()) {
                            Message msg = new Message("RespondRefuse", playerInvite);
                            sendMessage(msg);
                        } else {
                            String[] options = {"ACCEPT", "REFUSE"};
                            //Integer[] options = {1, 3, 5, 7, 9, 11};
                            //Double[] options = {3.141, 1.618};
                            //Character[] options = {'a', 'b', 'c', 'd'};
                            int x = JOptionPane.showOptionDialog(null, playerInvite.getUsername() + " challenges you",
                                    "",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//                        System.out.println(options[x]);

                            if (x == 0) { //ACCEPT CHALLENGE from player who sends invite
                                Message msg = new Message("RespondAccept", playerInvite);
                                opponent = playerInvite;
                                sendMessage(msg);

                                //start game
                                startGame();
                            } else { //REFUSE CHALLENGE from player who sends invite
                                Message msg = new Message("RespondRefuse", playerInvite);
                                sendMessage(msg);
                            }
                        }
                    }

                    if (msgReceived.getMessage().equals("Accept")) { //receives opponent's ACCEPT CHALLENGE
                        Player playerAccept = (Player) msgReceived.getData(); //player who accepts
                        mainFrm.getjLabel6().setText(playerAccept.getUsername() + " has accepted the challenge!");

                        //start game
                        startGame();
                    }

                    if (msgReceived.getMessage().equals("Refuse")) { //receives opponent's REFUSE CHALLENGE
                        Player playerRefuse = (Player) msgReceived.getData(); //player who refuses
                        mainFrm.getjLabel6().setText(playerRefuse.getUsername() + " has refused the challenge/is playing!");
                        opponent = null;
                    }

                    if (msgReceived.getMessage().equals("Question")) { //receives questions
                        ArrayList<Question> questions = new ArrayList<>();
                        questions = (ArrayList<Question>) msgReceived.getData();
                        quizFrm.setQuestions(questions);
                    }

                    if (msgReceived.getMessage().equals("StartGame")) {
                        Timer pause = new Timer(2000, new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Thread thread = new Thread(new GameEndListening());
                                thread.start();
                            }
                        });
                        pause.setRepeats(false);
                        pause.start();
                    }
                    if (msgReceived.getMessage().equals("ExitGame")) {
                        player.setPlaying(false);
                        opponent = null;
                        quizFrm.dispose();
                    }
                }
            } catch (Exception e) {
                try {
                    closeEverything(socket, ois, oos);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public class ScoreBoardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            scoreboardFrm = new ScoreboardFrm();
            scoreboardController = new ScoreboardController(scoreboardFrm);
            scoreboardFrm.setVisible(true);
        }

    }

    public void startGame() {
        quizFrm = new QuizFrm(player.getUsername());
        player.setPlaying(true);
        loadQuiz();
        quizFrm.addChoiceListener(new ChoiceListener());
        quizFrm.addStartListener(new StartListener());
        quizFrm.addCloseListener(new gameClosingListener());
        quizFrm.addTimeListenter(1000, new TimerListener());
        quizFrm.setVisible(true);
    }

    public void displayAnswer() {
        Timer timer = quizFrm.getTimer();
        JButton buttonA, buttonB, buttonC, buttonD;
        buttonA = quizFrm.getButtonA();
        buttonB = quizFrm.getButtonB();
        buttonC = quizFrm.getButtonC();
        buttonD = quizFrm.getButtonD();

        JLabel answer_labelA, answer_labelB, answer_labelC, answer_labelD;
        answer_labelA = quizFrm.getAnswer_labelA();
        answer_labelB = quizFrm.getAnswer_labelB();
        answer_labelC = quizFrm.getAnswer_labelC();
        answer_labelD = quizFrm.getAnswer_labelD();

        int index = quizFrm.getIndex();
        timer.stop();

        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        if (!quizFrm.getQuestions().get(index).getAnswer().equals("A")) {
            answer_labelA.setForeground(new Color(255, 0, 0));
        }
        if (!quizFrm.getQuestions().get(index).getAnswer().equals("B")) {
            answer_labelB.setForeground(new Color(255, 0, 0));
        }
        if (!quizFrm.getQuestions().get(index).getAnswer().equals("C")) {
            answer_labelC.setForeground(new Color(255, 0, 0));
        }
        if (!quizFrm.getQuestions().get(index).getAnswer().equals("D")) {
            answer_labelD.setForeground(new Color(255, 0, 0));
        }

        Timer pause = new Timer(2000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                answer_labelA.setForeground(new Color(25, 255, 0));
                answer_labelB.setForeground(new Color(25, 255, 0));
                answer_labelC.setForeground(new Color(25, 255, 0));
                answer_labelD.setForeground(new Color(25, 255, 0));

                quizFrm.setAnswer(" ");
                quizFrm.setSeconds(10);
                quizFrm.getSeconds_left().setText(String.valueOf(quizFrm.getSeconds()));
                buttonA.setEnabled(true);
                buttonB.setEnabled(true);
                buttonC.setEnabled(true);
                buttonD.setEnabled(true);
                quizFrm.setIndex(index + 1);
                nextQuestion();
            }
        });
        pause.setRepeats(false);
        pause.start();
    }

    public class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int seconds = quizFrm.getSeconds();
            seconds--;
            quizFrm.setSeconds(seconds);
            quizFrm.getSeconds_left().setText(String.valueOf(seconds));
            if (seconds <= 0) {
                displayAnswer();
            }
        }

    }

    public void results() {
        JButton buttonA, buttonB, buttonC, buttonD;
        buttonA = quizFrm.getButtonA();
        buttonB = quizFrm.getButtonB();
        buttonC = quizFrm.getButtonC();
        buttonD = quizFrm.getButtonD();

        int result = quizFrm.getResult();
        int correct_guesses = quizFrm.getCorrect_guesses();
        int total_questions = quizFrm.getTotal_questions();

        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        result = (int) ((correct_guesses / (double) total_questions) * 100);

        quizFrm.getTextfield().setText("RESULTS!");
        quizFrm.getTextarea().setText("");
        quizFrm.getAnswer_labelA().setText("");
        quizFrm.getAnswer_labelB().setText("");
        quizFrm.getAnswer_labelC().setText("");
        quizFrm.getAnswer_labelD().setText("");

        quizFrm.getNumber_right().setText("(" + correct_guesses + "/" + total_questions + ")");
        quizFrm.getPercentage().setText(result + "%");

        quizFrm.add(quizFrm.getNumber_right());
        quizFrm.add(quizFrm.getPercentage());

        quizFrm.setGameEnd(true);
        System.out.println("GameEnd");
    }

    public void nextQuestion() {
        int index = quizFrm.getIndex();
        int total_questions = quizFrm.getTotal_questions();

        if (index >= total_questions) {
            results();
        } else {
            quizFrm.getTextfield().setText("Question " + (index + 1));
            quizFrm.getTextarea().setText(quizFrm.getQuestions().get(index).getQuestion());
            quizFrm.getAnswer_labelA().setText(quizFrm.getQuestions().get(index).getOptions()[0]);
            quizFrm.getAnswer_labelB().setText(quizFrm.getQuestions().get(index).getOptions()[1]);
            quizFrm.getAnswer_labelC().setText(quizFrm.getQuestions().get(index).getOptions()[2]);
            quizFrm.getAnswer_labelD().setText(quizFrm.getQuestions().get(index).getOptions()[3]);
            quizFrm.getTimer().start();
        }
    }

    public class GameEndListening implements Runnable {

        @Override
        public void run() {
            nextQuestion();
            System.out.println("next roi");
            quizFrm.setTimeStart(System.nanoTime());
            while (!quizFrm.isGameEnd()) {

            }
            //gameEnd = true
//            System.out.println(quizFrm.isGameEnd());
            quizFrm.setTimeEnd(System.nanoTime());
            quizFrm.setTimelapsed((quizFrm.getTimeEnd() - quizFrm.getTimeStart()));

            //Finish game message
            ArrayList<Object> data = new ArrayList<>();
            data.add(quizFrm.getCorrect_guesses()); //index 0
            data.add(quizFrm.getTotal_questions()); //index 1
            data.add(quizFrm.getTimelapsed()); //index 2
            data.add(opponent); //index 3
            System.out.println(data.get(0) + "," + data.get(1) + "," + data.get(2) + "," + data.get(3));
            Message msg = new Message("RequestFinishGame", data);

            sendMessage(msg);
//            sendMessage(msg);
            System.out.println("gui roi");
        }
    }

    public void loadQuiz() {
        Message msg = new Message("RequestQuestion");
        sendMessage(msg);
    }

    public class ChoiceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton buttonA, buttonB, buttonC, buttonD;
            buttonA = quizFrm.getButtonA();
            buttonB = quizFrm.getButtonB();
            buttonC = quizFrm.getButtonC();
            buttonD = quizFrm.getButtonD();

            String answer = quizFrm.getAnswer();
            ArrayList<Question> questions = quizFrm.getQuestions();
            int index = quizFrm.getIndex();
            int correct_guesses = quizFrm.getCorrect_guesses();

            //begin
            buttonA.setEnabled(false);
            buttonB.setEnabled(false);
            buttonC.setEnabled(false);
            buttonD.setEnabled(false);

            if (e.getSource() == buttonA) {
                answer = "A";
                quizFrm.setAnswer(answer);
                if (answer.equals(questions.get(index).getAnswer())) {
                    correct_guesses++;
                }
            }
            if (e.getSource() == buttonB) {
                answer = "B";
                quizFrm.setAnswer(answer);
                if (answer.equals(questions.get(index).getAnswer())) {
                    correct_guesses++;
                }
            }
            if (e.getSource() == buttonC) {
                answer = "C";
                quizFrm.setAnswer(answer);
                if (answer.equals(questions.get(index).getAnswer())) {
                    correct_guesses++;
                }
            }
            if (e.getSource() == buttonD) {
                answer = "D";
                quizFrm.setAnswer(answer);
                if (answer.equals(questions.get(index).getAnswer())) {
                    correct_guesses++;
                }
            }

            quizFrm.setCorrect_guesses(correct_guesses);
            displayAnswer();
        }

    }

    public class StartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jButton = (JButton) e.getSource();
            jButton.setEnabled(false);

            Message msg = new Message("RequestStartGame", opponent);
            sendMessage(msg);
        }

    }

    @Override
    public void run() {
        listening();
    }

    public class UpdateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Message msg = new Message("RequestOnlineClient");
            sendMessage(msg);
        }

    }

    public class gameClosingListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            //if both finish the game, or both are not ready
            System.out.println("RequestExitGame");
            player.setPlaying(false);
            quizFrm.setGameEnd(true);
            Message msg = new Message("RequestExitGame", opponent);
            sendMessage(msg);
            opponent = null;
            e.getWindow().dispose();

            //if not
        }
    }

//    public class windowClosingListener extends WindowAdapter {
//
//        @Override
//        public void windowClosing(WindowEvent e) {
//            System.out.println("Closing...");
//            Message msg = new Message("RemoveOnlineClient");
//            sendMessage(msg);
////            e.getWindow().dispose();
//        }
//    }
    public class TableListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
//            System.out.println(player.isPlaying());

            JTable jTable1 = mainFrm.getjTable1();
            JLabel jLabel4 = mainFrm.getjLabel4();

            int column = jTable1.getColumnModel().getColumnIndexAtX(evt.getX()); // get the coloum of the button;
            int row = evt.getY() / jTable1.getRowHeight(); // get row 
//            System.out.println(row + " " + column);
//         *Checking the row or column is valid or not
            if (row < jTable1.getRowCount() && row >= 0 && column < jTable1.getColumnCount() && column >= 0) {
                currentPlayer = new Player();
                currentPlayer = mainFrm.getClientHandlers().get(row).getPlayer();
                jLabel4.setText(currentPlayer.getUsername());
            }
        }
    }

    public class ChallengeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(player.getUsername() + " challenges " + currentPlayer.getUsername());
            if (currentPlayer == null || currentPlayer.getUsername().equals(player.getUsername())) {
                mainFrm.getjLabel6().setText("Select a valid player!");
            } else {
                mainFrm.getjLabel6().setText("Sending invite to the selected player...");
                opponent = currentPlayer;
                Message msg = new Message("RequestInvite", opponent);
                sendMessage(msg);
            }
        }

    }

    public void closeEverything(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        Message msg = new Message("RemoveOnlineClient");
        oos.writeObject(msg);
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
}
