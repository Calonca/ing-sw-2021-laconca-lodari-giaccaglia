package it.polimi.ingsw.client.view.GUI.GUIelem;

import it.polimi.ingsw.server.model.Resource;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.List;

public class ResourceSphere extends Sphere {

    public ResourceSphere(double v, Resource resource) {
        super(v);
        this.resource = resource;
    }



    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    Resource resource;

    public void color()
    {
        setMaterial(new PhongMaterial(colors.get(resource.getResourceNumber())));
    }

    public static List<Color> colors=new ArrayList<>(){{
        add(Color.GOLD);
        add(Color.PURPLE);
        add(Color.BLUE);
        add(Color.RED);
        add(Color.RED);
        add(Color.DARKRED);
        add(Color.BLACK);
        add(Color.WHITE);
    }};
}
