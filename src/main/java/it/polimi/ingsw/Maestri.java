package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.GUI.GUI;
import it.polimi.ingsw.server.Server;
import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * Starts the Server, CLI or GUI based of the input arguments
 * The arguments are in the <a href="http://docopt.org/">docopt</a> standard.
 */
public class Maestri {

    public static final Option optServer = new Option("s","server",false,
            "Starts server with port");
    public static final Option optCLI = new Option("c","cli", false,
            "Starts the CLI and connects to server with port, ip and nickname\n" +
                    "without arguments it will let you choose them later"
    );
    public static final Option optGUI = new Option("g","gui", false,
            "Starts the GUI and connects to server with port, ip and nickname\n" +
                    "without arguments it will let you choose them later");


    public static void main(String[] args)  {
        Options options = new Options();
        options.addOption(optServer);
        options.addOption(optCLI);
        options.addOption(optGUI);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd  = parser.parse(options, args);
            if(cmd.hasOption(optCLI.getLongOpt()))
                guiAndCliHandler(true,options,args);
            else if(cmd.hasOption(optGUI.getLongOpt()))
                guiAndCliHandler(false,options,args);
            else if(cmd.hasOption(optServer.getLongOpt()))
                switch (args.length) {
                    case 1 -> Server.main(args);
                    case 2 -> {
                        try {
                            Integer.parseInt(args[1]);
                            Server.main(args);
                        }catch (NumberFormatException e){
                            printErrorAndClose("Insert a valid server port, must be an int",options);
                        }
                    }
                    default -> printErrorAndClose("Too many arguments for server",options);
                }
            else
                printErrorAndClose("Unrecognized command",options);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void guiAndCliHandler(boolean isCli, Options options,String[] args){
        switch (args.length) {
            case 1 -> initialize(isCli,args);
            case 2 -> printErrorAndClose("Insert both the port and the ip",options);
            case 3, 4 -> {
                try {
                    Integer.parseInt(args[1]);
                    initialize(isCli,args);
                } catch (NumberFormatException e) {
                    printErrorAndClose("Insert a valid server port, must be an int",options);
                }
            }
            default -> printErrorAndClose("Too many arguments for "+(isCli?"CLI":"GUI"),options);
        }
    }

    private static void initialize(boolean isCli, String[] args){
        Client client = new Client();
        client.setCLIOrGUI(isCli);
        if (args.length==4||args.length==3)
            client.setServerConnection(args[2], Integer.parseInt(args[1]));
        if (args.length==4) {
            client.getCommonData().setCurrentNick(args[3]);
        }
        if (isCli) client.run();
        else GUI.main(args);
    }

    private static void printErrorAndClose(String error,Options options){
        System.out.println(Color.colorString(error, Color.RED));
        displayHelpMessage(options);
        System.exit(-1);
    }

    private static void displayHelpMessage(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        PrintWriter printWriter = new PrintWriter(System.out);
        printWriter.println("Maestri del rinascimento");
        printWriter.println();
        helpFormatter.printUsage(printWriter,CLI.width,"java -jar Maestri.jar whatToStart [port] [ip] [nickname]");
        helpFormatter.printOptions(printWriter,CLI.width,options,2,5);
        printWriter.close();
    }

}
