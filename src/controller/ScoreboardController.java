/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import view.ScoreboardFrm;

/**
 *
 * @author typpo
 */
public class ScoreboardController {

    private ScoreboardFrm scoreboardFrm;
    private ArrayList<ArrayList<Object>> data;
    Connection con;

    public ScoreboardController(ScoreboardFrm scoreboardFrm) {
        this.scoreboardFrm = scoreboardFrm;
        data = new ArrayList<>();
        this.getDBConnection("quizgame", "root", "root");
        init();
    }

    private void getDBConnection(String dbName, String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=CONVERT_TO_NULL";
        try {
            con = DriverManager.getConnection(dbUrl, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        ArrayList<Object> dataRow;
        float totalSpareTime = 0;
        int totalWin = 0;
        float avgSpareTime = 0;

        String sql = "SELECT `Username`, `TotalScore`, `TotalMatch`, `TotalSpareTime`, `TotalWin` FROM quizgame.user;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataRow = new ArrayList<>();
                dataRow.add(rs.getString("Username"));
                dataRow.add(rs.getFloat("TotalScore"));
                dataRow.add(rs.getInt("TotalMatch"));

                totalSpareTime = rs.getFloat("TotalSpareTime");
                dataRow.add(totalSpareTime);

                totalWin = rs.getInt("TotalWin");
                dataRow.add(totalWin);
                
                avgSpareTime = totalSpareTime / totalWin;
                dataRow.add(avgSpareTime);
                data.add(dataRow);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        scoreboardFrm.setjTable1(data);
    }
}
