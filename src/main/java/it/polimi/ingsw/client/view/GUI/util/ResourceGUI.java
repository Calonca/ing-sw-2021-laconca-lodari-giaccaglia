package it.polimi.ingsw.client.view.GUI.util;

import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

import java.awt.*;
import java.util.Arrays;

public enum ResourceGUI {
    GOLD(ResourceAsset.GOLD ,"coin", Color.GOLD),
    SERVANT(ResourceAsset.SERVANT,  "servant", Color.PURPLE),
    SHIELD(ResourceAsset.SHIELD,  "shield", Color.CYAN),
    STONE(ResourceAsset.STONE, "stone", Color.GRAY),
    FAITH(ResourceAsset.FAITH, "faith",     Color.ORANGE),
    TO_CHOSE(ResourceAsset.TO_CHOOSE,"faith",Color.WHITE),
    EMPTY(ResourceAsset.EMPTY, "table",Color.BEIGE);


    private final ResourceAsset res;
    private final Color color;
    private final TriangleMesh mesh;

    ResourceGUI(ResourceAsset res, String name, Color c) {
        this.res = res;
        this.color = c;
        mesh = ModelImporter.getObjectWithName(name);
    }

    public ResourceAsset getRes() {
        return res;
    }

    public Shape3D generateShape() {
        Shape3D shape3d = new MeshView(mesh);
        shape3d.setScaleX(0.8);
        shape3d.setScaleY(0.8);
        shape3d.setScaleZ(0.8);
        ResourceGUI.setColor(this,shape3d,false,false);
        return shape3d;
    }

    public static void setColor(ResourceGUI res,Shape3D shape, boolean selected, boolean hoveringOver) {
        javafx.scene.paint.Color c2 = javafx.scene.paint.Color.hsb(res.color.getHue(),
                0.3,
                1);
        javafx.scene.paint.Color c = selected?c2:res.color;
        PhongMaterial shieldMaterial = new PhongMaterial(c);
        shape.setMaterial(shieldMaterial);
    }

    public static ResourceGUI fromAsset(ResourceAsset asset){
        return Arrays.stream(ResourceGUI.values()).filter(r->r.getRes().equals(asset)).findFirst().orElse(ResourceGUI.EMPTY);
    }
}
