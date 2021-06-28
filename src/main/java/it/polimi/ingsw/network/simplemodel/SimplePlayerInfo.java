package it.polimi.ingsw.network.simplemodel;

public class SimplePlayerInfo {

    private final int currentVictoryPoints;
    private final int currentPosition;
    private final int currentLorenzoPosition;
    private final boolean isOnline;
    private final int playerIndex;
    private final String nickname;

    public SimplePlayerInfo(int currentVictoryPoints, int currentPosition, int currentLorenzoPosition, boolean isOnline, int playerIndex, String nickname) {
        this.currentVictoryPoints = currentVictoryPoints;
        this.currentPosition = currentPosition;
        this.currentLorenzoPosition = currentLorenzoPosition;
        this.isOnline = isOnline;
        this.playerIndex = playerIndex;
        this.nickname = nickname;
    }

    public int getCurrentVictoryPoints() {
        return currentVictoryPoints;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getLorenzoPosition() {
        return currentLorenzoPosition;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public String getNickname() {
        return nickname;
    }


}
