package test;


import slythr.Render;
import slythr.Shader;

import java.util.Random;
import java.util.Vector;

public class geometryShader extends Shader {


    private static int pos;

    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int zPos = 2;
    private static final int width = 3;
    private static final int height = 4;
    private static double x;
    private static double y;
    private static double z;

//    Random random = new Random();
//    Vec3d avec = new Vec3d();
//    Vec3d thetavec = new Vec3d();
//    Vec3d dvec = new Vec3d();




    public int[] shader(int[] artifact) {

//        avec.set(artifact[xPos], artifact[yPos], artifact[zPos]);
//        dvec.x = (1) * (Math.cos(Render.cameraAngle.x) - Math.sin(Render.cameraAngle.y)) * (Math.cos(Render.cameraAngle.z) + Math.sin(Render.cameraAngle.z)) * (avec.x - Render.cameraPos.x);
//
//        avec.x =


        return artifact;
    }

}
