package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.URL;
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

        final int boardWidth=2402;
        final int boardHeight=1717;
        Rectangle board = new Rectangle(boardHeight, boardHeight);
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

        scene.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.DOWN){
                Translate t = new Translate();
                t.setZ(-100);
                camera.getTransforms().add(t);
            }
        });

        scene.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.UP){
                Translate t = new Translate();
                t.setZ(100);
                camera.getTransforms().add(t);
            }
        });

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

        double[] stoneVertsGenerated = new double[0];
        try {
            stoneVertsGenerated=getGeneratedModel("table").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Float[] stoneVertsF = Arrays.stream(stoneVertsGenerated).mapToObj(v->(float)v).toArray(Float[]::new);
        float[] fA = ArrayUtils.toPrimitive(stoneVertsF);


        int[] facesStartingFrom1Generated = new int[0];
        try {

            double[] listToInt=getGeneratedModel("table").get(1);
            //todo is it possible to avoid cast?
            facesStartingFrom1Generated=new int[listToInt.length];
            for(int i=0;i< listToInt.length;i++)
                facesStartingFrom1Generated[i]=(int) listToInt[i];

        } catch (IOException e) {
            e.printStackTrace();
        }


        Integer[] facesFromZeroBoxed = Arrays.stream(facesStartingFrom1Generated).boxed()
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


    /**
     * The method dinamically gets the path of the given resource name
     * @param res is a resource which .OBJ exist
     * @return the corresponding value for 3D models
     * @throws IOException if res is not an adequate parameter
     */
    public static List<double[]> getGeneratedModel(String res) throws IOException {
        List<Double> verts=new ArrayList<>();
        List<Integer> smoothings=new ArrayList<>();


        String path="/assets/3dAssets/" + res + ".OBJ";
        InputStreamReader reader = new InputStreamReader(BoardView3D.class.getResourceAsStream(path));

        Scanner br=new Scanner(reader);
        br.nextLine();
        br.nextLine();

        String line;
        String[] numbers;

        while (br.hasNextLine())
        {
            line=br.nextLine();
            if(line.isEmpty())
                break;
            line=line.substring(2);
            numbers= line.split("\\d\\s+");

            for (String number : numbers) verts.add(Double.valueOf(number));

        }
        while (br.hasNextLine())
        {
            line=br.nextLine();
            if(line.isEmpty())
                break;

        }
        while (br.hasNextLine())
        {
            line=br.nextLine();
            if(line.isEmpty())
                break;

        }
        br.nextLine();
        while (br.hasNextLine())
        {
            line=br.nextLine();
            if (line.isEmpty())
                break;
            if (line.length()>3)
            line=line.substring(2);
            if(line.charAt(0)!='s')
            {
                numbers= line.split("/\\d\\d?\\d?\\d?\\d?\\s?+");
                for(String toAdd : numbers)
                {
                    smoothings.add(Integer.valueOf(toAdd));
                    smoothings.add(0);
                    System.out.println(toAdd);

                }
            }


        }


        // Covert the lists to double arrays
        List<double[]> vertsAndSmoothing=new ArrayList<>();
        // print out just for verification
        double[] vertArray = new double[verts.size()];
        // ArrayList to Array Conversion
        for (int k =0; k < verts.size(); k++)
            vertArray[k] = verts.get(k);

        vertsAndSmoothing.add(vertArray);

        double[] intArray = new double[smoothings.size()];
        // ArrayList to Array Conversion
        for (int k =0; k < smoothings.size(); k++)
            intArray[k] = smoothings.get(k);


        //System.out.println(toReturnInt.size());
        vertsAndSmoothing.add(intArray);

        //System.out.println(Arrays.toString(vertsAndSmoothing.get(0)));
        //System.out.println(Arrays.toString(vertsAndSmoothing.get(1)));

        return vertsAndSmoothing;
    }

}
