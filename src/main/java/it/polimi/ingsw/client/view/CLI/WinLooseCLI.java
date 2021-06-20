package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;

import java.beans.PropertyChangeEvent;

public class WinLooseCLI extends WinLooseBuilder implements CLIBuilder {
    @Override
    public void run() {
        getCLIView().setTitle("You won or lost");
        Row root=new Row();

        for(int i=0;i<getEndGameInfo().getPlayersEndingTheGame().size();i++)
        {
            EndGameInfo.PlayerInfo pi=getEndGameInfo().getPlayerInfo(getEndGameInfo().getPlayersEndingTheGame().get(i));
            Drawable drawable=new Drawable();
            drawable.add(new DrawableLine(0, 0, pi.getPlayerNickname(), Color.BRIGHT_WHITE, Background.ANSI_GREEN_BACKGROUND));
            drawable.add(new DrawableLine(0, 1, Integer.toString(pi.getVictoryPoints()), Color.BRIGHT_WHITE, Background.ANSI_GREEN_BACKGROUND));
            drawable.add(new DrawableLine(0, 2, Integer.toString(pi.getFaithTrackPosition()), Color.BRIGHT_WHITE, Background.ANSI_GREEN_BACKGROUND));
            Column tempCol=new Column();
            tempCol.addElem(Option.noNumber(drawable));
            root.addElem(tempCol);
        }
        getCLIView().setBody(CanvasBody.centered(root));

        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
