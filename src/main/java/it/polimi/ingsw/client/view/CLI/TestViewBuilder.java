package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.HorizontalListBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestViewBuilder extends it.polimi.ingsw.client.view.abstractview.TestViewBuilder implements CLIBuilder {

    private HorizontalListBody hor;

    @Override
    public void run() {

        getCLIView().setTitle(new Title("Testing horizontal list, to start the cli remove comments in client"));
        AtomicInteger resSelected = new AtomicInteger();

        HorizontalList horList = new HorizontalList(getCLIView().getMaxBodyHeight());

        horList.addOption(Option.from("12345678901234567890\nname1","subtitle1",()->{
            resSelected.getAndIncrement();
            getCLIView().setTitle(new Title("Selected res "+resSelected.get()));
            getCLIView().setBody(hor);
            getCLIView().show();
        }));

        Drawable d = new Drawable();
        d.add(new DrawableLine(0,0,"I'm a black title", Color.RIGHT_WHITE, Background.ANSI_BLACK_BACKGROUND));
        d.add(new DrawableLine(3,1,"I'm a red body of text\nshifted to the left", Color.RED, Background.DEFAULT));
        horList.addOption(Option.from(d,()->{
            resSelected.getAndIncrement();
            getCLIView().setTitle(new Title("Selected res "+resSelected.get()));
            getCLIView().setBody(hor);
            getCLIView().show();
        }));

        Drawable d2 = new Drawable();
        d2.add(new DrawableLine(0,0,"I'm a square", Color.BLACK, Background.ANSI_YELLOW_BACKGROUND));
        d2.addEmptyLine();
        d2.add(0,"hi");
        d2.shift(4,0);

        Drawable d3 = new Drawable();
        d3.add(new DrawableLine(0,0,"+--------+", Color.BLACK, Background.ANSI_CYAN_BACKGROUND));
        d3.add(0,"|        |",Color.BLACK, Background.ANSI_CYAN_BACKGROUND);
        d3.add(0,"|   hi   |",Color.BLACK, Background.ANSI_CYAN_BACKGROUND);
        d3.add(0,"|________|",Color.BLACK, Background.ANSI_CYAN_BACKGROUND);
        d3 = Drawable.copyShifted(5,3,d3);

        horList.addOption(Option.from(new Drawable(d2,d3),()->{
            resSelected.getAndIncrement();
            getCLIView().setTitle(new Title("Selected res "+resSelected.get()));
            getCLIView().setBody(hor);
            getCLIView().show();
        }));

        hor = new HorizontalListBody(horList);
        getCLIView().setBody(hor);


        String compactMessage = "Press send to compact, times left: ";
        String widenMessage = "Press send to widen, times left: ";


        Canvas ca = horList.getCanvas();
        ca.setDebugging(false);
        System.out.println(ca.toString());
        //getCLIView().refreshCLI();
        Thread t = new Thread(()-> {
            int times=10;
            for (int i=0;i<times;i++) {
                List<Drawable> dwll = ca.getListsInRow(5);
                dwll.forEach(dwl -> dwl.shift(-5, 0));
                String s = ca.toString();
                getCLIView().deleteText();
                System.out.println(s);
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        getCLIView().runOnInput("Start animation", t::start);


        //getCLIView().setBody(new CanvasBody(ca));
        //getCLIView().refreshCLI();

    }
    private Runnable getCompactCode(String widenMessage,String compactMessage,int timesLeft){
        String wd = widenMessage+timesLeft;
        return ()->{
            if (timesLeft==0){
                getCLIView().setBody(hor);//To show options
                getCLIView().show();
            } else {
                hor.getHorizontalList().setMode(HorizontalList.Mode.COMPACT);
                getCLIView().runOnInput(wd, getWdCode(compactMessage, widenMessage, timesLeft - 1));
                getCLIView().show();
            }
        };
    }

    private Runnable getWdCode(String compact,String widen,int timesLeft){
        return ()->{
            if (timesLeft==0){
                getCLIView().setBody(hor);//To show options
                getCLIView().show();
            } else {
                hor.getHorizontalList().setMode(HorizontalList.Mode.WIDE);
                getCLIView().runOnInput(compact+timesLeft, getCompactCode(widen, compact, timesLeft - 1));
            }
            getCLIView().show();
        };
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
    }
}
