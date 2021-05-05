package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OptionList extends CLIelem{
    private List<Option> options;

    public OptionList(){
        options = new ArrayList<>();
        setUpdater(()-> {});
    }

    public OptionList(Stream<Option> optionStream){
        options = optionStream.collect(Collectors.toList());
        if (options.size()>=1)
            options.get(0).setSelected(true);
        setUpdater(()-> {});
    }

    public void addOption(Option o){
        options.add(o);
        if (options.size()==1)
            o.setSelected(true);
    }

    public void updateOptions(Stream<Option> optionStream,Client client){
        if (options!=null)
            options.forEach(o->o.removeFromPublishers(client));
        options = optionStream.collect(Collectors.toList());
        if (options.size()>=1)
            options.get(0).setSelected(true);
        if (cli!=null)
        {
            setCLIAndAddToPublishers(cli,client);
        }
    }

    private void selectOptionAtPosition(int numberInList)
    {
        options.forEach(option -> option.setSelected(false));
        options.get(numberInList).setSelected(true);
        cli.setLastChoice(Optional.of(options.get(numberInList)));
    }
    public void selectOption()
    {
        Runnable r = ()->{
            int choice = cli.getLastInt();
            selectOptionAtPosition(choice);
            cli.performLastChoice();
        };
        cli.runOnIntInput("Select a choice:","Select a valid choice",0,options.size()-1,r);
    }

    public Stream<String> toStringStream() {
        return IntStream.range(0,options.size())
                .mapToObj(i->i+": "+options.get(i).toString());
    }

    @Override
    public void setCLIAndAddToPublishers(CLI cli, Client client) {
        super.setCLIAndAddToPublishers(cli, client);
        options.forEach(o->o.setCLIAndAddToPublishers(cli,client));
    }


}
