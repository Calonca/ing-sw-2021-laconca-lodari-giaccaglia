package it.polimi.ingsw.server.messages.messagebuilders;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage.elementAdapter;

public class tem {

    public static double[] getStoneVertsGenerated(){

        List<Double> toReturn=new ArrayList<>();

        String path="/assets/3dAssets/stone.OBJ";
        InputStreamReader reader = new InputStreamReader(tem.class.getResourceAsStream(path));

        Scanner br=new Scanner(reader);
        br.nextLine();
        br.nextLine();

        String line;
        String[] numbers;

        while (true)
        {
            line=br.nextLine();
            if(line.isEmpty())
                break;
            line=line.substring(2);
            numbers= line.split("\\d\\s+");

            for (String number : numbers) toReturn.add(Double.valueOf(number));

        }
        // Covert the lists to double arrays

        // print out just for verification
        double[] arr = new double[toReturn.size()];

        // ArrayList to Array Conversion
        for (int k =0; k < toReturn.size(); k++)
            arr[k] = toReturn.get(k);
        return arr;
    }



    public static void main(String[] args){

        double[] stones = getStoneVertsGenerated();
        int ciao= 9;

    }
}




