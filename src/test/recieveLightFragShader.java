package test;

import slythr.*;

public class recieveLightFragShader extends Shader {

    public int[] shader(int[] artifact) {

        for (Primitive primitive : Engine.rendStack) {
            if (primitive.properties.contains(testGame.light)) {
                artifact[2] = artifact[2] + 1 / SMath.getDistance_int(artifact[0], artifact[1], Primitive.roundAndCast(primitive.getpos()[0]), Primitive.roundAndCast(primitive.getpos()[1]));
                artifact[3] = artifact[3] + 1 / SMath.getDistance_int(artifact[0], artifact[1], Primitive.roundAndCast(primitive.getpos()[0]), Primitive.roundAndCast(primitive.getpos()[1]));
                artifact[4] = artifact[4] + 1 / SMath.getDistance_int(artifact[0], artifact[1], Primitive.roundAndCast(primitive.getpos()[0]), Primitive.roundAndCast(primitive.getpos()[1]));

            }
        }

        return artifact;
    }

}
