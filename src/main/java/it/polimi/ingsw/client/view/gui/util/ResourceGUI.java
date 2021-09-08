package it.polimi.ingsw.client.view.gui.util;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

import java.util.Arrays;

public enum ResourceGUI {
    GOLD(ResourceAsset.GOLD ,"coin", Color.GOLD),
    SERVANT(ResourceAsset.SERVANT,  "servant", Color.PURPLE),
    SHIELD(ResourceAsset.SHIELD,  "shield", Color.CYAN),
    STONE(ResourceAsset.STONE, "stone", Color.GRAY),
    FAITH(ResourceAsset.FAITH, "faith",     Color.ORANGE),
    TO_CHOSE(ResourceAsset.TO_CHOOSE,"table",Color.WHITE),
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
        Shape3D shape3d = ModelImporter.getShape3d(mesh);
        shape3d.setScaleX(0.8);
        shape3d.setScaleY(0.8);
        shape3d.setScaleZ(0.8);
        ResourceGUI.setColor(this,shape3d,false,true);
        return shape3d;
    }

    public static void setColor(ResourceGUI res,Shape3D shape, boolean selected, boolean selectable) {
        javafx.scene.paint.Color selColor = javafx.scene.paint.Color.hsb(
                res.color.getHue(),
                0.3,
                1);
        javafx.scene.paint.Color disabled = javafx.scene.paint.Color.hsb(
                res.color.getHue(),
                1,
                0.2);
        javafx.scene.paint.Color c = selected? selColor:(selectable? res.color:disabled);
        PhongMaterial shieldMaterial = new PhongMaterial(c);
        shape.setMaterial(shieldMaterial);
    }

    public static Shape3D addAndGetShape(Group parentAndRefSystem, ResourceGUI res, Point3D shift) {
        Shape3D stoneMesh = res.generateShape();
        NodeAdder.addNodeToParent(parentAndRefSystem,stoneMesh,shift);
        return stoneMesh;
    }

    public static ResourceGUI fromAsset(ResourceAsset asset){
        return Arrays.stream(ResourceGUI.values()).filter(r->r.getRes().equals(asset)).findFirst().orElse(ResourceGUI.EMPTY);
    }
}
