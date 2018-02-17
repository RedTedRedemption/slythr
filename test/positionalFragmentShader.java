package test;

import slythr.SMath;
import slythr.Shader;

public class positionalFragmentShader extends Shader {

    private static final int r = 3;
    private static final int g = 4;
    private static final int b = 5;
    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int zPos = 2;

    public int[] shader(int[] artifact) {
        artifact[r] = artifact[xPos];
        artifact[g] = artifact[yPos];
        System.out.println(artifact[r]);
        artifact[r] = artifact[r] + 1 / SMath.getDistance_int(artifact[xPos], artifact[yPos], threadingDemo.game_window.getMousePosition().x, threadingDemo.game_window.getMousePosition().y);
        artifact[g] = artifact[g] + 1 / SMath.getDistance_int(artifact[xPos], artifact[yPos], threadingDemo.game_window.getMousePosition().x, threadingDemo.game_window.getMousePosition().y);
        artifact[b] = artifact[b] + 1 / SMath.getDistance_int(artifact[xPos], artifact[yPos], threadingDemo.game_window.getMousePosition().x, threadingDemo.game_window.getMousePosition().y);

        System.out.println(artifact[r]);
        return artifact;
    }

}
