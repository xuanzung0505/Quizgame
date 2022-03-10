/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import view.LoginFrm;

/**
 *
 * @author typpo
 */
public class LoginController {

    private LoginFrm loginFrm;
    Connection con;

    private final String host = "localhost";
    private final int port = 8888;

    public LoginController(LoginFrm loginFrm) {
        this.loginFrm = loginFrm;
        loginFrm.addLoginListener(new LoginListener());
        loginFrm.setVisible(true);
        this.getDBConnection("quizgame", "root", "root");
    }

    private void getDBConnection(String dbName, String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=CONVERT_TO_NULL";
        try {
            con = DriverManager.getConnection(dbUrl, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Player p = null;

            //1. get user info
            String username = loginFrm.getjTextField1().getText();
            String password = String.valueOf(loginFrm.getjPasswordField1().getPassword());

            String sql = "SELECT * FROM quizgame.user\n"
                    + "WHERE Username = ?\n"
                    + "AND Password = ?;";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    p = new Player();
                    p.setId(rs.getInt("Id"));
                    p.setUsername(rs.getString("Username"));
                    p.setPassword(rs.getString("Password"));
                    p.setState("online"); //Set state to online
                    p.setTotalScore(rs.getFloat("TotalScore"));
                    p.setTotalMatch(rs.getInt("TotalMatch"));
                    p.setTotalSpareTime(rs.getFloat("TotalSpareTime"));
                    p.setTotalWin(rs.getInt("TotalWin"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (p != null) {
                try {
                    Socket socket = new Socket(host, port);
                    System.out.println("New socket");
                    ClientController clientController = new ClientController(p, socket);
                    clientController.start();
                    
                    loginFrm.setVisible(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {

            }
        }
    }
}
