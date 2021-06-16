package it.polimi.ingsw.client.view.GUI.util;

import javafx.scene.shape.Shape3D;

import java.util.ArrayList;
import java.util.List;

public class DragAndDropData {
    protected Shape3D shape3D;
    protected ResourceGUI resourceGUI = ResourceGUI.EMPTY;
    protected Runnable onDrop = ()->{};
    protected boolean available;
    protected Integer globalPos;
    protected List<Integer> availablePos = new ArrayList<>();

    public Shape3D getShape3D() {
        return shape3D;
    }

    public void setShape3D(Shape3D shape3D) {
        this.shape3D = shape3D;
    }

    public ResourceGUI getResourceGUI() {
        return resourceGUI;
    }

    public void setResourceGUI(ResourceGUI resourceGUI) {
        this.resourceGUI = resourceGUI;
    }

    public Runnable getOnDrop() {
        return onDrop;
    }

    public void setOnDrop(Runnable onDrop) {
        this.onDrop = onDrop;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getGlobalPos() {
        return globalPos;
    }

    public void setGlobalPos(Integer globalPos) {
        this.globalPos = globalPos;
    }

    public List<Integer> getAvailablePos() {
        return availablePos;
    }

    public void setAvailablePos(List<Integer> availablePos) {
        this.availablePos = availablePos;
    }
}
