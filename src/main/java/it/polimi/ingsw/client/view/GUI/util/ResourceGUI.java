package it.polimi.ingsw.client.view.GUI.util;

import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

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
    private final String objName;
    private final Color color;
    private Shape3D shape;

    ResourceGUI(ResourceAsset res, String name, Color c) {
        this.res = res;
        objName = name;
        this.color = c;
    }

    public ResourceAsset getRes() {
        return res;
    }

    public Shape3D generateShape() {
        shape = ModelImporter.getObjectWithName(objName);
        shape.setScaleX(0.8);
        shape.setScaleY(0.8);
        shape.setScaleZ(0.8);
        ResourceGUI.getSelected(this,false,false);
        return shape;
    }

    public static ResourceGUI getSelected(ResourceGUI res, boolean selected, boolean hoveringOver) {
        if (res.shape==null)
            res.generateShape();
        javafx.scene.paint.Color c2 = javafx.scene.paint.Color.hsb(res.color.getHue(),
                0.3,
                1);
        javafx.scene.paint.Color c = selected?c2:res.color;
        PhongMaterial shieldMaterial = new PhongMaterial(c);
        res.shape.setMaterial(shieldMaterial);
        return res;
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