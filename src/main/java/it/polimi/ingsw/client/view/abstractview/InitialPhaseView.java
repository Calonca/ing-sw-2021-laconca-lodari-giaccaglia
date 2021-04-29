package it.polimi.ingsw.client.view.abstractview;

public abstract class InitialPhaseView extends View {
    public InitialPhaseView(String leaderAndChooseNumber) {
        this.leaderAndChooseNumber = leaderAndChooseNumber;
    }

    protected String leaderAndChooseNumber;//Todo make into treeMap
}
