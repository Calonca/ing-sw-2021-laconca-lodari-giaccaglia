package it.polimi.ingsw.client.view.GUI;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoardView3D extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView{

    Camera camera;
    public AnchorPane boardPane;
    double buttonStartingX=100;
    double width=1800;
    double len=1000;
    Box toPut;


    @Override
    public void run() {
        Node root=getRoot();
        //todo fix initialization
        root.setId("3DVIEW");

        GUI.getRealPane().getChildren().add(root);

        System.out.println(GUI.getRealPane().getChildren());

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

        return new SubScene(root,width,len);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PerspectiveCamera testCamera = new PerspectiveCamera();

        Group parent = new Group();

        Rectangle board = new Rectangle(2402, 1717);
        Image boardPic = new Image("assets/board/biggerboard.png");
        board.setFill(new ImagePattern(boardPic));
        board.setTranslateX(-500);
        board.setTranslateY(-500);
        board.setTranslateZ(3000);
        parent.getChildren().add(board);

        Color color = Color.GRAY;
        MeshView stone = objConverter("default");
        setMaterial(stone, color,false);
        Point3D boardTopLeft = board.localToParent(new Point3D(0,0,0));

        stone.setTranslateX(boardTopLeft.getX());
        stone.setTranslateY(boardTopLeft.getY());
        stone.setTranslateZ(boardTopLeft.getZ());
        Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
        Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
        stone.getTransforms().addAll(rotate1,rotate2);
        parent.getChildren().add(stone);




        AtomicBoolean dragStarted = new AtomicBoolean(false);
        stone.setOnDragDetected((MouseEvent event)-> {
            if (!dragStarted.get()) {
                dragStarted.set(true);
                setMaterial(stone,color,true);
            }
            stone.setMouseTransparent(true);
            board.setMouseTransparent(false);
            stone.setCursor(Cursor.MOVE);
            stone.startFullDrag();
        });

        stone.setOnMouseReleased((MouseEvent event)-> {
            stone.setMouseTransparent(false);
            dragStarted.set(false);
            setMaterial(stone,color,false);
            board.setMouseTransparent(true);
            stone.setCursor(Cursor.DEFAULT);
        });

        board.setOnMouseDragOver((MouseEvent event)->{
            Point3D stoneCoords = event.getPickResult().getIntersectedPoint();
            stoneCoords = board.localToParent(stoneCoords);

            //stoneCoords = boardTopLeft;
            stone.setTranslateX(stoneCoords.getX());
            stone.setTranslateY(stoneCoords.getY());
            stone.setTranslateZ(stoneCoords.getZ());
            //Translate translate = new Translate(stoneCoords.getX(),stoneCoords.getY(),stoneCoords.getZ());
            //shield.getTransforms().add(translate);
        });


        SubScene scene = new SubScene(parent, width, len);
        testCamera.setTranslateZ(-1000);
        scene.setCamera(testCamera);



        Button viewCardShop=new Button();
        viewCardShop.setLayoutX(buttonStartingX);
        viewCardShop.setLayoutY(50);
        parent.getChildren().add(viewCardShop);
        viewCardShop.setText("VIEW CARD SHOP");
        viewCardShop.setOnMouseEntered( p ->
        {
            //todo fix static
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
            {
                viewCardShop.setDisable(true);
                return;
            }
            else
                viewCardShop.setDisable(false);
            CardShopGUI cs=new CardShopGUI();
            cs.run();
        });

        viewCardShop.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
                return;
            GUI.removeLast();

        });



        Button viewMarket=new Button();
        viewMarket.setLayoutX(buttonStartingX);
        viewMarket.setLayoutY(100);
        viewMarket.setText("VIEW MARKET");
        viewMarket.setOnMouseEntered( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
            {
                viewMarket.setDisable(true);
                return;
            }
            else
                viewMarket.setDisable(false);
            ResourceMarketGUI cs=new ResourceMarketGUI();
            cs.run();
        });

        viewMarket.setOnMouseExited( p ->
        {
            if(ViewPersonalBoard.getController().isMarket()|| ViewPersonalBoard.getController().isCardShopOpen())
                return;
            GUI.removeLast();
        });

        System.out.println(GUI.getRealPane().getChildren());

        viewMarket.setId("showButton");
        viewCardShop.setId("showButton");
        boardPane.getChildren().add(viewCardShop);
        boardPane.getChildren().add(viewMarket);
        boardPane.getChildren().add(scene);
    }


    /**
     * Converts an obj file to a MeshView
     */
    public static MeshView objConverter(String fileName){

        int[] facesStartingFrom1 = {
                1,0,  2,0,  3,0,
                4,0,  5,0,  6,0,
                7,0,  8,0,  9,0,
                10,0, 11,0, 12,0,
                13,0, 14,0, 15,0,
                16,0, 17,0, 18,0,
                19,0, 20,0, 21,0,
                22,0, 23,0, 24,0,
                25,0, 26,0, 27,0,
                28,0, 29,0, 30,0,
                31,0, 32,0, 33,0,
                34,0, 35,0, 36,0,
                37,0, 38,0, 39,0,
                40,0, 41,0, 42,0,
                43,0, 44,0, 45,0,
                46,0, 47,0, 48,0,
                49,0, 50,0, 51,0,
                52,0, 53,0, 54,0,
                55,0, 56,0, 57,0,
                58,0, 59,0, 60,0
        };


        double[] stoneVertsGenerated = new double[0];
        try {
            stoneVertsGenerated=getStoneVertsGenerated();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (double value : stoneVertsGenerated) System.out.println(value);

        Float[] stoneVertsF = Arrays.stream(stoneVertsGenerated).mapToObj(v->(float)v).toArray(Float[]::new);
        float[] fA = ArrayUtils.toPrimitive(stoneVertsF);

        try {
            getStoneVertsGenerated();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer[] facesFromZeroBoxed = Arrays.stream(facesStartingFrom1).boxed()
                .map(i->i==0?0:i-1)
                .toArray(Integer[]::new);
        int[] facesFromZero = ArrayUtils.toPrimitive(facesFromZeroBoxed);

        TriangleMesh stone = new TriangleMesh();
        stone.getPoints().setAll(fA);
        stone.getTexCoords().addAll(0,0);
        stone.getFaces().addAll(facesFromZero);



        return new MeshView(stone);
    }

    @NotNull
    private void setMaterial(Shape3D shield, Color color, boolean selected) {
        Color c2 = Color.hsb(color.getHue(),
                0.3,
                1);
        Color c = selected?c2:color;
        PhongMaterial shieldMaterial = new PhongMaterial(c);
        shield.setMaterial(shieldMaterial);
    }

    public static double[] getStoneVertsGenerated() throws IOException {
        Scanner inputStream = null;
        List<Double> toReturn=new ArrayList<>();

        try
        {
            URL url = BoardView3D.class.getResource("stone.OBJ");
            String path="C:\\Users\\gianm\\Documents\\GitHub\\A\\src\\main\\resources\\assets\\3dAssets\\stone.OBJ";
            File file = new File(path);
            //File file = new File(url.getPath());  doesnt work. needs relative path???

            Scanner br=new Scanner(file);
            br.nextLine();
            br.nextLine();

            String line = null;
            String[] numbers = null;

            int i=0;
            while (true)
            {
                line=br.nextLine();
                if(line.isEmpty())
                    break;
                line=line.substring(2);
                numbers= line.split("\\d\\s+");

                for (String number : numbers) toReturn.add(Double.valueOf(number));

            }
            // Covert the lists to double arrays




            // print out just for verification
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        double[] arr = new double[toReturn.size()];

        // ArrayList to Array Conversion
        for (int k =0; k < toReturn.size(); k++)
            arr[k] = toReturn.get(k);
        return arr;
    }
}
