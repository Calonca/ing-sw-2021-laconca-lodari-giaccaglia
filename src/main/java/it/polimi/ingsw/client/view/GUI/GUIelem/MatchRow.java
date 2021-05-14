package it.polimi.ingsw.client.view.GUI.GUIelem;

import java.util.UUID;

public class MatchRow {
    private String people[];
    private int number;
    private UUID key;

    public MatchRow(int number, String[] people, UUID key) {
        this.people=people;
        this.number=number;
        this.key=key;
    }
    public UUID getKey() {
        return key;
    }

    public String[] getPeople() {
        return people;
    }

    public int getNumber() {
        return number;
    }
}
