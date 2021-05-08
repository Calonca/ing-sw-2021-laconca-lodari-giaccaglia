package it.polimi.ingsw.client.view.GUI.GUIelem;

public class MatchRow {
    private String[] people;
    private int number;

    public MatchRow(int number, String[] people) {
        this.people=people;
        this.number=number;
    }

    public String[] getPeople() {
        return people;
    }

    public int getNumber() {
        return number;
    }
}
