package example1;

import slythr.Shader;

public class positionalFragShader extends Shader {

    public int[] shader(int[] artifact) {
        artifact[3] = artifact[0];
        artifact[4] = artifact[1];
        artifact[5] = artifact[5];
        return artifact;
    }

}
