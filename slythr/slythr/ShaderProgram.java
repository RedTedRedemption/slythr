package slythr;

public class ShaderProgram {

    public Shader[] pipeline = new Shader[3];

    public ShaderProgram() {

    }

    public void linkShader(int shaderType, Shader shader) {
        pipeline[shaderType] = shader;
    }

}
