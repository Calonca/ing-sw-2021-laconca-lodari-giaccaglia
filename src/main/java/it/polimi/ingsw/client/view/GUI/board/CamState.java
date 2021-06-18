package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public enum CamState {
    TOP(new Point3D(0,0,0),new Point3D(0,0,0)),
    LOOK_AT_OTHERS(new Point3D(-500,5000,-1400),new Point3D(75,0,0)),
    SEE_SHOP_MARKET(new Point3D(-500,-3300,600),new Point3D(45,0,0));

    private final Point3D pos;
    private final Point3D rot;
    private boolean moveFreely=false;


    CamState(Point3D pos, Point3D rot) {
        this.pos = pos;
        this.rot =rot;
    }

    public Point3D getPos() {
        return pos;
    }

    public Point3D getRot() {
        return rot;
    }

    public void animateWithKeyCode(PerspectiveCamera camera, KeyCode keyCode){
        if (keyCode == KeyCode.TAB) {
            moveFreely = !moveFreely;
            System.out.println("Cam mode is : "+(moveFreely?"move freely":"animation"));
            return;
        }

        if (moveFreely) {
            Translate t = new Translate();
            Rotate r =  new Rotate(0, new Point3D(1, 0, 0));
            if (keyCode == KeyCode.W) {
                t.setY(100);
            } else if (keyCode == KeyCode.S) {
                t.setY(-100);
            } else if (keyCode == KeyCode.A) {
                t.setX(100);
            } else if (keyCode == KeyCode.D) {
                t.setX(-100);
            }
            else if (keyCode == KeyCode.Q) {
                t.setZ(100);
            } else if (keyCode == KeyCode.E) {
                t.setZ(-100);
            }
            else if (keyCode == KeyCode.R) {
                r.setAngle(5);
            } else if (keyCode == KeyCode.F) {
                r.setAngle(-5);
            }
            camera.setTranslateX(camera.getTranslateX()+t.getX());
            camera.setTranslateY(camera.getTranslateY()+t.getY());
            camera.setTranslateZ(camera.getTranslateZ()+t.getZ());
            camera.setRotationAxis(r.getAxis());
            camera.setRotate(camera.getRotate()+r.getAngle());
            System.out.println("Cam position state: "+name()+", x: "+camera.getTranslateX()+", y: "+camera.getTranslateY()+", z:"+camera.getTranslateZ()+", rot:"+camera.getRotate());
        } else {
            if (keyCode == KeyCode.W) {
                if (equals(CamState.TOP)) {
                    animateToState(camera, CamState.LOOK_AT_OTHERS);
                } else if  (equals(CamState.LOOK_AT_OTHERS)){
                    animateToState(camera, CamState.SEE_SHOP_MARKET);
                }
            } else if (keyCode == KeyCode.S) {
                if (equals(CamState.LOOK_AT_OTHERS)) {
                    animateToState(camera, CamState.TOP);
                }else if  (equals(CamState.SEE_SHOP_MARKET)){
                    animateToState(camera, CamState.LOOK_AT_OTHERS);
                }
            }
        }
    }


    public void animateToState(PerspectiveCamera camera, CamState nextState) {
        Translate t = new Translate();
        Rotate rotateCam1 = new Rotate(0, new Point3D(1, 0, 0));
        Rotate rotateCam2 = new Rotate(0, new Point3D(0, 1, 0));
        Rotate rotateCam3 = new Rotate(0, new Point3D(0, 0, 1));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(t.xProperty(),getPos().getX()),
                        new KeyValue(t.yProperty(),getPos().getY()),
                        new KeyValue(t.zProperty(),getPos().getZ()),
                        new KeyValue(rotateCam1.angleProperty(),getRot().getX())
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(t.xProperty(), nextState.getPos().getX(), Interpolator.EASE_BOTH),
                        new KeyValue(t.yProperty(), nextState.getPos().getY(),Interpolator.EASE_BOTH),
                        new KeyValue(t.zProperty(), nextState.getPos().getZ(),Interpolator.EASE_BOTH),
                        new KeyValue(rotateCam1.angleProperty(), nextState.getRot().getX(),Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
        camera.getTransforms().setAll(t, rotateCam1);
        BoardView3D.setCamState(nextState);
    }
}
