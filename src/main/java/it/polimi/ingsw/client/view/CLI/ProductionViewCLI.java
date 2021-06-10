package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.Row;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;

import java.util.*;

public class ProductionViewCLI extends ProductionViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_PRODUCTION);
        board.initializeFaithTrack(getSimpleModel());
        board.initializeBuyOrChoosePos();
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        SimpleProductions simpleProductions = getThisPlayerCache().getElem(SimpleProductions.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells, simpleProductions);
        board.setProductions(prodsRow);
        board.setMessage("Select a production");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    @Override
    public void choosingResForProduction() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_CARD_SHOP);
        SimpleFaithTrack[] simpleFaithTracks = Arrays.stream(getSimpleModel().getPlayersCaches())
                .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
        board.setTop(new FaithTrackGridElem(getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks));
        Row prodsRow = new Row();
        board.setProductions(prodsRow);
        //NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();
        //prodsRow.addElem(Option.noNumber(DrawableDevCard.fromDevCardAsset(card)));
        //List<ResourceAsset> costs = card.getCostList().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
        //        .sorted(Comparator.comparing(ResourceAsset::getResourceNumber)).collect(Collectors.toList());
        //board.setResChoiceRow(new ResChoiceRow(0,costs,new ArrayList<>()));
        board.initializeBuyOrChoosePos();
        board.setMessage("Select resources for the production, only working for warehouse for now");
        getCLIView().setBody(board);
        getCLIView().show();
    }
}
