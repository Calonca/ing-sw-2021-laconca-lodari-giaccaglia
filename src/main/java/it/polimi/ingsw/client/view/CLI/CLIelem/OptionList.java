package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OptionList extends CLIelem{
    private List<Option> options;

    public OptionList(){
        options = new ArrayList<>();
        setUpdater(()-> cliView.update());
    }

    public void addOption(Option o){
        o.setCLIView(cliView);
        options.add(o);
        if (options.size()==1)
            o.setSelected(true);
    }

    private void selectOptionAtPosition(int numberInList)
    {
        options.forEach(option -> option.setSelected(false));
        options.get(numberInList).setSelected(true);
        cliView.setLastChoice(Optional.of(options.get(numberInList)));
    }
    public void selectOption()
    {
        int choice;
        do  {
            try
            {
                String in = cliView.getIN("Please insert a number");
                choice = Integer.parseInt(in);
                if (choice<0||choice>=options.size())
                {
                    if (choice<0)
                        cliView.printError("DON'T insert a NEGATIVE number!");
                    else cliView.printError("Insert a SMALLER number!");
                }else {
                    break;
                }
            }
            catch (NumberFormatException e){
                cliView.printError("Insert a NUMBER!");
            }
        }while(true);
        selectOptionAtPosition(choice);
        cliView.displayWithScroll();
    }

    public Stream<String> toStringStream() {
        return IntStream.range(0,options.size())
                .mapToObj(i->i+": "+options.get(i).toString());
    }

}
