/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import model.Question;

/**
 *
 * @author HP
 */
public class QuizFrm extends javax.swing.JFrame {

    private ArrayList<Question> questions = new ArrayList<>();
    char guess;
    String answer;
    int index;
    int correct_guesses;
    int total_questions;
    int result;
    int seconds;
    long timeStart;
    long timeEnd;
    long timelapsed;

    JTextField number_right = new JTextField();
    JTextField percentage = new JTextField();
    Timer timer;

    volatile boolean gameEnd;

    /**
     * Creates new form QuizFrm
     */
    public QuizFrm(String title) {
        super(title);
        initComponents();
        this.getContentPane().setBackground(new Color(50, 50, 50));
        this.setLayout(null);
        this.setResizable(true);

        number_right.setBounds(225, 225, 200, 100);
        number_right.setBackground(new Color(25, 25, 25));
        number_right.setForeground(new Color(25, 255, 0));
        number_right.setFont(new Font("Ink Free", Font.BOLD, 50));
        number_right.setBorder(BorderFactory.createBevelBorder(1));
        number_right.setHorizontalAlignment(JTextField.CENTER);
        number_right.setEditable(false);

        percentage.setBounds(225, 325, 200, 100);
        percentage.setBackground(new Color(25, 25, 25));
        percentage.setForeground(new Color(25, 255, 0));
        percentage.setFont(new Font("Ink Free", Font.BOLD, 50));
        percentage.setBorder(BorderFactory.createBevelBorder(1));
        percentage.setHorizontalAlignment(JTextField.CENTER);
        percentage.setEditable(false);

        seconds = 10;
        correct_guesses = 0;
        total_questions = 0;
        index = 0;
        timeStart = 0;
        timeEnd = 0;
        timelapsed = 0;
        gameEnd = false;
    }

    public void addTimeListenter(int miliseconds, ActionListener al) {
        timer = new Timer(miliseconds, al);
    }

    public void addCloseListener(WindowAdapter wa) {
        this.addWindowListener(wa);
    }

    public void addStartListener(ActionListener al) {
        jButton1.addActionListener(al);
    }

    public void addChoiceListener(ActionListener al) {
        buttonA.addActionListener(al);
        buttonB.addActionListener(al);
        buttonC.addActionListener(al);
        buttonD.addActionListener(al);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
        total_questions = questions.size();
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public int getCorrect_guesses() {
        return correct_guesses;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public String getAnswer() {
        return answer;
    }

    public JButton getButtonA() {
        return buttonA;
    }

    public JButton getButtonB() {
        return buttonB;
    }

    public JButton getButtonC() {
        return buttonC;
    }

    public JButton getButtonD() {
        return buttonD;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public void setCorrect_guesses(int correct_guesses) {
        this.correct_guesses = correct_guesses;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getTimelapsed() {
        return timelapsed;
    }

    public void setTimelapsed(long timelapsed) {
        this.timelapsed = timelapsed;
    }

    public JLabel getAnswer_labelA() {
        return answer_labelA;
    }

    public JLabel getAnswer_labelB() {
        return answer_labelB;
    }

    public JLabel getAnswer_labelC() {
        return answer_labelC;
    }

    public JLabel getAnswer_labelD() {
        return answer_labelD;
    }

    public JTextArea getTextarea() {
        return textarea;
    }

    public JTextField getTextfield() {
        return textfield;
    }

    public Timer getTimer() {
        return timer;
    }

    public char getGuess() {
        return guess;
    }

    public int getResult() {
        return result;
    }

    public JTextField getNumber_right() {
        return number_right;
    }

    public JTextField getPercentage() {
        return percentage;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public JLabel getSeconds_left() {
        return seconds_left;
    }

    public void setSeconds_left(JLabel seconds_left) {
        this.seconds_left = seconds_left;
    }

    public JLabel getTime_label() {
        return time_label;
    }

    public void setTime_label(JLabel time_label) {
        this.time_label = time_label;
    }

    public void setGuess(char guess) {
        this.guess = guess;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTotal_questions(int total_questions) {
        this.total_questions = total_questions;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setNumber_right(JTextField number_right) {
        this.number_right = number_right;
    }

    public void setPercentage(JTextField percentage) {
        this.percentage = percentage;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setAnswer_labelA(JLabel answer_labelA) {
        this.answer_labelA = answer_labelA;
    }

    public void setAnswer_labelB(JLabel answer_labelB) {
        this.answer_labelB = answer_labelB;
    }

    public void setAnswer_labelC(JLabel answer_labelC) {
        this.answer_labelC = answer_labelC;
    }

    public void setAnswer_labelD(JLabel answer_labelD) {
        this.answer_labelD = answer_labelD;
    }

    public void setButtonA(JButton buttonA) {
        this.buttonA = buttonA;
    }

    public void setButtonB(JButton buttonB) {
        this.buttonB = buttonB;
    }

    public void setButtonC(JButton buttonC) {
        this.buttonC = buttonC;
    }

    public void setButtonD(JButton buttonD) {
        this.buttonD = buttonD;
    }

    public void setjButton1(JButton jButton1) {
        this.jButton1 = jButton1;
    }

    public void setTextarea(JTextArea textarea) {
        this.textarea = textarea;
    }

    public void setTextfield(JTextField textfield) {
        this.textfield = textfield;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textfield = new javax.swing.JTextField();
        textarea = new javax.swing.JTextArea();
        buttonA = new javax.swing.JButton();
        buttonB = new javax.swing.JButton();
        buttonC = new javax.swing.JButton();
        buttonD = new javax.swing.JButton();
        answer_labelA = new javax.swing.JLabel();
        answer_labelB = new javax.swing.JLabel();
        answer_labelC = new javax.swing.JLabel();
        answer_labelD = new javax.swing.JLabel();
        time_label = new javax.swing.JLabel();
        seconds_left = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(680, 650));

        textfield.setEditable(false);
        textfield.setBackground(new java.awt.Color(25, 25, 25));
        textfield.setFont(new java.awt.Font("Ink Free", 1, 30)); // NOI18N
        textfield.setForeground(new java.awt.Color(25, 255, 0));
        textfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textfield.setAlignmentX(0.0F);
        textfield.setAlignmentY(0.0F);
        textfield.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        textfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textfieldActionPerformed(evt);
            }
        });

        textarea.setEditable(false);
        textarea.setBackground(new java.awt.Color(25, 25, 25));
        textarea.setColumns(20);
        textarea.setFont(new java.awt.Font("MV Boli", 1, 25)); // NOI18N
        textarea.setForeground(new java.awt.Color(25, 255, 0));
        textarea.setLineWrap(true);
        textarea.setRows(5);
        textarea.setWrapStyleWord(true);
        textarea.setAlignmentX(0.0F);
        textarea.setAlignmentY(50.0F);
        textarea.setBorder(null);

        buttonA.setFont(new java.awt.Font("MV Boli", 1, 35)); // NOI18N
        buttonA.setText("A");
        buttonA.setAlignmentY(100.0F);
        buttonA.setFocusable(false);

        buttonB.setFont(new java.awt.Font("MV Boli", 1, 35)); // NOI18N
        buttonB.setText("B");
        buttonB.setAlignmentY(200.0F);
        buttonB.setFocusable(false);

        buttonC.setFont(new java.awt.Font("MV Boli", 1, 35)); // NOI18N
        buttonC.setText("C");
        buttonC.setAlignmentY(300.0F);
        buttonC.setFocusable(false);

        buttonD.setFont(new java.awt.Font("MV Boli", 1, 35)); // NOI18N
        buttonD.setText("D");
        buttonD.setAlignmentY(400.0F);
        buttonD.setFocusable(false);

        answer_labelA.setBackground(new java.awt.Color(50, 50, 50));
        answer_labelA.setFont(new java.awt.Font("MV Boli", 0, 35)); // NOI18N
        answer_labelA.setForeground(new java.awt.Color(25, 255, 0));
        answer_labelA.setAlignmentX(125.0F);
        answer_labelA.setAlignmentY(100.0F);

        answer_labelB.setBackground(new java.awt.Color(50, 50, 50));
        answer_labelB.setFont(new java.awt.Font("MV Boli", 0, 35)); // NOI18N
        answer_labelB.setForeground(new java.awt.Color(25, 255, 0));
        answer_labelB.setAlignmentX(125.0F);
        answer_labelB.setAlignmentY(200.0F);

        answer_labelC.setBackground(new java.awt.Color(50, 50, 50));
        answer_labelC.setFont(new java.awt.Font("MV Boli", 0, 35)); // NOI18N
        answer_labelC.setForeground(new java.awt.Color(25, 255, 0));
        answer_labelC.setAlignmentX(125.0F);
        answer_labelC.setAlignmentY(300.0F);

        answer_labelD.setBackground(new java.awt.Color(50, 50, 50));
        answer_labelD.setFont(new java.awt.Font("MV Boli", 0, 35)); // NOI18N
        answer_labelD.setForeground(new java.awt.Color(25, 255, 0));
        answer_labelD.setAlignmentX(125.0F);
        answer_labelD.setAlignmentY(400.0F);

        time_label.setBackground(new java.awt.Color(50, 50, 50));
        time_label.setFont(new java.awt.Font("MV Boli", 0, 16)); // NOI18N
        time_label.setForeground(new java.awt.Color(255, 0, 0));
        time_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        time_label.setText("Timer");
        time_label.setAlignmentX(535.0F);
        time_label.setAlignmentY(475.0F);

        seconds_left.setBackground(new java.awt.Color(25, 25, 25));
        seconds_left.setFont(new java.awt.Font("Ink Free", 1, 60)); // NOI18N
        seconds_left.setForeground(new java.awt.Color(255, 0, 0));
        seconds_left.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seconds_left.setText("10");
        seconds_left.setAlignmentX(535.0F);
        seconds_left.setAlignmentY(510.0F);
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);

        jButton1.setFont(new java.awt.Font("MV Boli", 1, 24)); // NOI18N
        jButton1.setText("READY");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textfield, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(textarea)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seconds_left, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(time_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(answer_labelD, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(answer_labelC, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(answer_labelB, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(answer_labelA, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textfield, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textarea, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(answer_labelA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(answer_labelB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(answer_labelC, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(answer_labelD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(time_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seconds_left, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        textarea.getAccessibleContext().setAccessibleParent(this);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void textfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textfieldActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(QuizFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(QuizFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(QuizFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(QuizFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                QuizFrm quizFrm = new QuizFrm("Quiz");
//                Controller controller = new Controller(quizFrm);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel answer_labelA;
    private javax.swing.JLabel answer_labelB;
    private javax.swing.JLabel answer_labelC;
    private javax.swing.JLabel answer_labelD;
    private javax.swing.JButton buttonA;
    private javax.swing.JButton buttonB;
    private javax.swing.JButton buttonC;
    private javax.swing.JButton buttonD;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel seconds_left;
    private javax.swing.JTextArea textarea;
    private javax.swing.JTextField textfield;
    private javax.swing.JLabel time_label;
    // End of variables declaration//GEN-END:variables

//    @Override
//    public void run() {
//        nextQuestion();
//    }
}
