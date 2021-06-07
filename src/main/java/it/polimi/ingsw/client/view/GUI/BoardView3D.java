package it.polimi.ingsw.client.view.GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardView3D extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView{

    Camera camera;
    public AnchorPane boardPane;
    private double anchorX, anchorY;
    //Keep track of current angle for x and y
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //We will update these after drag. Using JavaFX property to bind with object
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    Sphere toPut;


    @Override
    public void run() {
        Node toAdd=getRoot();
        //todo fix initialization
        toAdd.setId("3DVIEW");


    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Board3d.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,1000,700);

    }

    private void initMouseControl(Group group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;

        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            System.out.println(toPut.getTranslateX() + "Y" + toPut.getTranslateY());
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            System.out.println(toPut.getTranslateX() + "Y" + toPut.getTranslateY());

            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
            System.out.println(angleX);
            System.out.println(angleY);
        });
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Rotate(0,Rotate.Z_AXIS),new Rotate(0,Rotate.X_AXIS), new Rotate(0,Rotate.Y_AXIS), new Translate(0, 0, 0d));
        camera.translateXProperty().set(20);
        camera.translateYProperty().set(20.0);
        camera.setTranslateZ(-20);
        Group root3D = new Group();



        for(int i=0;i<100;i++)
        {
        toPut=new Sphere(10);
        toPut.translateYProperty().set(i);
        toPut.translateXProperty().set(i);
        root3D.getChildren().add(toPut);
        }
        Scene scene=new Scene(root3D,1000,700);
        scene.setCamera(camera);
        initMouseControl(root3D,scene);

        getClient().getStage().setScene(scene);
    }
}
