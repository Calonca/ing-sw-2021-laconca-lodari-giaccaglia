package it.polimi.ingsw.client.view.GUI.GUIelem;

import it.polimi.ingsw.server.model.Resource;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class ResourceButton extends Button {
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void color()
    {
        setStyle(resourceToRest.get(this.resource.getResourceNumber()));
    }
    Resource resource;
    public static List<String> resourceToRest=new ArrayList<>(){{
        add(" -fx-background-color: #B8860B");
        add(" -fx-background-color: #9400D3");
        add(" -fx-background-color: #0099FF");
        add(" -fx-background-color: #DEB887");
        add(" -fx-background-color: #ffffff");
        add(" -fx-background-color: #ffffff");
        add(" -fx-background-color: #ffffff");
        add(" -fx-background-color: #ffffff");
    }};
}
