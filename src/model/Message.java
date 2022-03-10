/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author typpo
 */
public class Message implements Serializable {

    private String message;
    private Object data;

    /*
    Server(ClientHandler) - Client
    1."OnlineClient"
    2."Invite" //send invitation to player
    3."Refuse" //send refuse invitation to player who invites
    4."Accept" // send accept invitation to player who invites
    5."Question"
    6."StartGame" //allow both clients to start game -> clienthandler.player.setPlaying = "true"
    7."ExitGame" //when game is not finished but client exited -> clienthandler.player.setPlaying = "false"
    
    Client - Server(ClientHandler)
    1."RequestOnlineClient"
    2."RequestInvite" //invite the selected player
    3."RespondRefuse" //refuse the invitation (on click "REFUSE" or isplaying = "true")
    4."RespondAccept" //accept the invitation
    5."RequestQuestion"
    6."RequestStartGame" //click "READY" -> set player.playing to "true"
    7."RequestExitGame" //exited when game is not finished  -> set player.playing to "false"
    8."RespondGameFinish" //game is finished  -> set player.playing to "true"
    */
    public Message(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
