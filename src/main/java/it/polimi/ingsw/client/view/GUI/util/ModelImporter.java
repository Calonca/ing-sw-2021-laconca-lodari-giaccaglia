package it.polimi.ingsw.client.view.GUI.util;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ModelImporter {

    /**
     * Converts an obj file to correctly rotated trianglemesh
     */
    public static MeshView getShape3d(String filename){
        return getShape3d(getObjectWithName(filename));
    }

    /**
     * Converts an imported TriangleMesh to correctly rotated trianglemesh
     */
    public static MeshView getShape3d(TriangleMesh mesh){
        MeshView toRet = new MeshView(mesh);
        Rotate rotate1 = new Rotate(270   ,new Point3D(1,0,0));
        Rotate rotate2 = new Rotate(270   ,new Point3D(0,1,0));
        toRet.getTransforms().addAll(rotate1,rotate2);
        return toRet;
    }

    /**
     * Converts an obj file to a TriangleMesh
     */
    public static TriangleMesh getObjectWithName(String fileName){

        double[] stoneVertsGenerated = new double[0];
        try {
            stoneVertsGenerated=getGeneratedModel(fileName).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Float[] stoneVertsF = Arrays.stream(stoneVertsGenerated).mapToObj(v->(float)v).toArray(Float[]::new);
        float[] fA = ArrayUtils.toPrimitive(stoneVertsF);


        int[] facesStartingFrom1Generated = new int[0];
        try {

            double[] listToInt=getGeneratedModel(fileName).get(1);
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



        return stone;
    }


    /**
     * The method dinamically gets the path of the given resource name
     * @param res is a resource which .OBJ exist
     * @return the corresponding value for 3D models
     * @throws IOException if res is not an adequate parameter
     */
    private static List<double[]> getGeneratedModel(String res) throws IOException {
        List<Double> verts=new ArrayList<>();
        List<Integer> smoothings=new ArrayList<>();


        String path="/assets/3dAssets/" + res + ".OBJ";
        InputStreamReader reader = new InputStreamReader(ModelImporter.class.getResourceAsStream(path));

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
                    //System.out.println(toAdd);

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
