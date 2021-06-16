package it.polimi.ingsw.network.messages.clienttoserver;

public class SendNickname extends ClientToServerMessage{

    protected String nickname;

    public SendNickname(String nickname){
        this.nickname = nickname;
    }


}
