/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.LoginController;
import view.LoginFrm;

/**
 *
 * @author typpo
 */
public class ClientMain {
    public static void main(String[] args) {
        LoginController loginController = new LoginController(new LoginFrm());
    }
}
