package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.ResChoiceRow;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductionViewCLI extends ProductionViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_PRODUCTION);
        board.initializeFaithTrack(getSimpleModel());
        board.initializeBuyOrChoosePos();
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Select a production or press ENTER to produce");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    @Override
    public void choosingResForProduction() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_RES_FOR_PROD);
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        int lastSelectedProduction = simpleCardCells.getSimpleProductions().getLastSelectedProductionPosition();
        Map<ResourceAsset, Integer> inputRes =  simpleCardCells.getProductionAtPos(lastSelectedProduction).map(SimpleProductions.SimpleProduction::getInputResources).orElse(new HashMap<>());
        Map<ResourceAsset, Integer> outputRes = simpleCardCells.getProductionAtPos(lastSelectedProduction).map(SimpleProductions.SimpleProduction::getOutputResources).orElse(new HashMap<>());
        List<ResourceAsset> input = inputRes.entrySet().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue())).collect(Collectors.toList());
        List<ResourceAsset> output = outputRes.entrySet().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue())).collect(Collectors.toList());
        ResChoiceRow choices = new ResChoiceRow(0,input,output);
        Row top = new Row(Stream.of(new SizedBox(1,0),choices.getGridElem()));
        board.setTop(top);
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setResChoiceRow(choices);
        board.initializeBuyOrChoosePos();
        board.setMessage("Select the resources for the production");
        getCLIView().setBody(board);
        getCLIView().show();
    }
}
