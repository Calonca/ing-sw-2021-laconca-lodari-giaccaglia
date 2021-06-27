package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.Playground;
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
    TOP(new Point3D(0,-300,-900),new Point3D(0,0,0)),
    LOOK_AT_OTHERS(new Point3D(-500,5000,-3500),new Point3D(75,0,0)),
    LOOK_AT_SECOND(new Point3D(0,0,-4000),new Point3D(0,0,270)),
    LOOK_AT_THIRD(new Point3D(0,0,-4000),new Point3D(0,0,180)),
    LOOK_AT_FOURTH(new Point3D(0,0,-4000),new Point3D(0,0,90)),
    SEE_SHOP_MARKET(new Point3D(-500,300,-1700),new Point3D(45,0,0)),
    SEE_SHOP(new Point3D(-1000,300,-1700),new Point3D(45,0,0)),
    SELECT_CARD_SHOP(new Point3D(-200,0,-600),new Point3D(0,0,0)),
    SEE_RESOURCE_MARKET(new Point3D(0,300,-1700),new Point3D(45,0,0));


    private final Point3D pos;
    private final Point3D rot;
    private boolean moveFreely=false;
    private static PerspectiveCamera camera;
    private CamState w,a,s,d;


    //Called whe the class is initialized
    static {
        TOP.w=LOOK_AT_OTHERS;

        LOOK_AT_OTHERS.w=SEE_SHOP_MARKET;
        LOOK_AT_OTHERS.s=TOP;
        LOOK_AT_OTHERS.d=LOOK_AT_SECOND;
        LOOK_AT_OTHERS.a=LOOK_AT_FOURTH;

        LOOK_AT_SECOND.d=LOOK_AT_THIRD;
        LOOK_AT_SECOND.a=LOOK_AT_OTHERS;

        LOOK_AT_THIRD.d=LOOK_AT_FOURTH;
        LOOK_AT_THIRD.a=LOOK_AT_SECOND;

        LOOK_AT_FOURTH.d=LOOK_AT_OTHERS;
        LOOK_AT_FOURTH.a=LOOK_AT_THIRD;

        SEE_SHOP_MARKET.w = TOP;
        SEE_SHOP_MARKET.s=TOP;
    }

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

    public static void setCamera(PerspectiveCamera camera) {
        CamState.camera = camera;
    }

    public void animateWithKeyCode(KeyCode keyCode){
        if (keyCode == KeyCode.T) {
            moveFreely = !moveFreely;
            System.out.println("Cam mode is : "+(moveFreely?"move freely":"animation"));
            System.out.println(pos);
            return;
        }

        if (moveFreely) {
            Translate t = new Translate();
            Rotate r =  new Rotate(0, new Point3D(0, 0, 1));
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
                animateToState(w);
            } else if (keyCode == KeyCode.S) {
                animateToState(s);
            }else if (keyCode == KeyCode.A) {
                animateToState(a);
            }
            else if (keyCode == KeyCode.D) {
                animateToState(d);
            }
        }
    }

    private boolean animationInPlaying=false;
    public void animateToState(CamState nextState) {
        if (nextState==null)
            return;
        Translate t =  new Translate();
        Point3D tbc= Playground.getTableCenter();
        Rotate rotateCam1 = new Rotate(0, tbc.getX(),tbc.getY(),tbc.getZ(),Rotate.X_AXIS);
        Rotate rotateCam2 = new Rotate(0, new Point3D(0, 1, 0));
        Rotate rotateCam3 = new Rotate(0, tbc.getX(),tbc.getY(),tbc.getZ(),Rotate.Z_AXIS);

        if (!camera.getTransforms().isEmpty() && camera.getTransforms().get(0) instanceof Translate)
            t = (Translate) camera.getTransforms().get(0);
        if (!camera.getTransforms().isEmpty() && camera.getTransforms().get(1) instanceof Rotate)
            rotateCam1.setAngle(((Rotate) camera.getTransforms().get(1)).getAngle());
        if (!camera.getTransforms().isEmpty() && camera.getTransforms().get(2) instanceof Rotate)
            rotateCam3.setAngle(((Rotate) camera.getTransforms().get(2)).getAngle());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(t.xProperty(),t.getX()),
                        new KeyValue(t.yProperty(),t.getY()),
                        new KeyValue(t.zProperty(),t.getZ()),
                        new KeyValue(rotateCam1.angleProperty(),rotateCam1.getAngle()),
                        new KeyValue(rotateCam3.angleProperty(),(nextState.getRot().getZ()>180&&rotateCam3.getAngle()==0)?360:rotateCam3.getAngle())
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(t.xProperty(), nextState.getPos().getX(), Interpolator.EASE_BOTH),
                        new KeyValue(t.yProperty(), nextState.getPos().getY(),Interpolator.EASE_BOTH),
                        new KeyValue(t.zProperty(), nextState.getPos().getZ(),Interpolator.EASE_BOTH),
                        new KeyValue(rotateCam1.angleProperty(), nextState.getRot().getX(),Interpolator.EASE_BOTH),
                        new KeyValue(rotateCam3.angleProperty(), (nextState.getRot().getZ()==0&&getRot().getZ()>180)?360:nextState.getRot().getZ(),Interpolator.EASE_BOTH)
                )
        );
        animationInPlaying= true;
        timeline.play();
        timeline.setOnFinished(e->animationInPlaying=false);
        camera.getTransforms().setAll(t,rotateCam1,rotateCam3);
        Playground.setCamState(nextState);
    }
}
