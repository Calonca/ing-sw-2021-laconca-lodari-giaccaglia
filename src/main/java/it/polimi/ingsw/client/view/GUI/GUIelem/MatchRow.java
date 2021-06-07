package it.polimi.ingsw.client.view.GUI.GUIelem;

import java.util.UUID;

public class MatchRow {
    private final String people;
    private final UUID key;

    public MatchRow(UUID key, String people) {
        this.people=people;
        this.key=key;
    }
    public UUID getKey() {
        return key;
    }

    public String getPeople() {
        return people;
    }
}
