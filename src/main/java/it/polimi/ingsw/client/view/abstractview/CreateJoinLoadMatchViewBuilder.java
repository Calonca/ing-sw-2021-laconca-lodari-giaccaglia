package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.match.CreateJoinLoadMatchCLI;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.gui.CreateJoinLoadMatchGUI;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class CreateJoinLoadMatchViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new CreateJoinLoadMatchCLI();
        else return new CreateJoinLoadMatchGUI();
    }

    /**
     * Method to get information regarding active and joinable matches
     * @param uuidPair is the match's UUID
     * @return the match's players information
     */

    public static Pair<String, String> idAndNames(Map.Entry<UUID, Pair<String[], String[]>> uuidPair){
        String matchIdString = "Match ID : " + uuidPair.getKey().toString().substring( 0, 8 );
        String coloredNames;
        if (getClient().isCLI())
            coloredNames = coloredNames( uuidPair.getValue().getKey(), uuidPair.getValue().getValue() );
        else coloredNames = uncoloredNames( uuidPair.getValue().getKey(), uuidPair.getValue().getValue() );
        return new Pair<>(matchIdString,coloredNames);
    }


    /**
     * This is a method to get pre-formatted colored names
     * @param onlineNames are online players' names
     * @param offlineNames are offline players' names
     * @return the string colored of corresponding colors for online and offline players
     */
    private static String coloredNames(String[] onlineNames, String[] offlineNames){

        String[] coloredOnlineNames;
        String[] coloredOfflineNames;

        if(onlineNames.length>0)
            coloredOnlineNames = Arrays
                    .stream(onlineNames)
                    .filter(Objects::nonNull)
                    .map( name -> Color.colorString( name, Color.BRIGHT_GREEN) )
                    .toArray( String[]::new);
        else
            coloredOnlineNames = new String[]{Color.colorString( "No players online", Color.BRIGHT_RED )};

        if(offlineNames.length>0) {
            coloredOfflineNames = Arrays
                    .stream( offlineNames )
                    .filter( Objects::nonNull )
                    .map( name -> Color.colorString( name, Color.BRIGHT_RED ) )
                    .toArray( String[]::new );
        }

        else
            coloredOfflineNames = new String[]{Color.colorString( "No players offline", Color.BRIGHT_RED )};


        return Arrays.toString(ArrayUtils.addAll( coloredOnlineNames, coloredOfflineNames));

    }

    private static String uncoloredNames(String[] onlineNames, String[] offlineNames){

        String[] coloredOnlineNames;
        String[] coloredOfflineNames;

        if(onlineNames.length>0)
            coloredOnlineNames = Arrays
                    .stream(onlineNames)
                    .filter(Objects::nonNull)
                    .toArray( String[]::new);
        else
            coloredOnlineNames = new String[]{ "No players online"};

        if(offlineNames.length>0) {
            coloredOfflineNames = Arrays
                    .stream( offlineNames )
                    .filter( Objects::nonNull )
                    .toArray( String[]::new );
        }

        else
            coloredOfflineNames = new String[]{ "No players offline"};


        return Arrays.toString(ArrayUtils.addAll( coloredOnlineNames, coloredOfflineNames));

    }

    @Override
    public void propertyChange (PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();
        if (propertyName.equals(CommonData.matchesDataString))
            run();
    }

}
