package slythr;


import java.awt.*;

/**
 * Instances of this class render as filled rectangles on the window.
 */


public class Rect extends Primitive {

	public int color_r = 255;
	public int color_g = 255;
	public int color_b = 255;
	double center_x;
	double center_y;
	double center_z;
	public double origin_x = 0;
	public double origin_y = 0;
	public double origin_z = 0;
	public double height = 20;
	public double width = 20;
	public double physics_velocity_x = 0;
	public double physics_velocity_y = 0;
	public boolean enabled = true;
	public Animation self_animation;
	public boolean sprite = false;
	public int sprite_step;
	public String label = "a rect object";
	private boolean GLEnabled = true;

	Vertex_Array vertex_arrayAB;
	Vertex_Array vertex_arrayBC;
	Vertex_Array vertex_arrayCD;
	Vertex_Array vertex_arrayDA;



	public Vertex_Array vertex_array = new Vertex_Array(Render.FILL_RECT, 8, shaderProgram, makeVertexData());


	/**
	 * Rect constructor. Engine must be initialized or engine will kill the program.
	 */
	public Rect() {
		if (!Engine.initialized) {
			Engine.throwFatalError(new SlythrError("ERROR: Slythr must be launched before primitives can be created"));
		}
		Engine.rendStack.add(this);
		shaderProgram = plainShaderProgram;
		if (GLEnabled) {
			vertex_array = Render.bindVertexArray(Render.FILL_RECT, 8, shaderProgram, makeVertexData());
		}
	}

	public Rect(boolean GLDisabled) {
        GLEnabled = false;
		Engine.rendStack.add(this);
	}


	/**
	 * Set the attributes of the rect quickly.
	 * @param x X position of the upper left corner
	 * @param y Y position of the upper left corner
	 * @param Height Height of the rect
	 * @param Width Width of the rect
	 * @param r Color value for the Red channel between 0 and 255
	 * @param g Color value for the Green channel between 0 and 255
	 * @param b Color value for the Blue channel between 0 and 255
	 */
	public void setAttributes(double x, double y, double Height, double Width, int r, int g, int b) {

		origin_x = x;
		origin_y = y;
		height = Height;
		width = Width;
		color_r = r;
		color_g = g;
		color_b = b;
		center_x = x - (width / 2.0);
		center_y = y - (height / 2.0);
		sprite_step = 0;



		/*
		System.out.print("the attributes of object ");
		System.out.print(this);
		System.out.print(" are ");
		System.out.print(origin_x + " ");
		System.out.print(origin_y + " ");
		System.out.print(height + " ");
		*/

	}


	/**
	 * Draws the rectangle. Should rarely be called directly. In most cases this method will be called by a {@link Stack Stack}.
	 * @param g Graphics instance on which the rect will be drawn.
	 */
	public void draw(Graphics g) {

        if (!enabled) {
            return;
        }
        if (!GLEnabled) {
			g.setColor(new Color(color_r, color_g, color_b));
			g.fillRect(roundAndCast(origin_x), roundAndCast(origin_y), roundAndCast(width), roundAndCast(height));
		} else {
        	vertex_array.enabled = enabled;
            //vertex_arrayAB.setData(new int[] {((int) (origin_x / origin_z)), ((int) (origin_y / origin_z)), (int) (origin_x + width), ((int) origin_y), color_r, color_g, color_b});
            //vertex_arrayBC.setData(new int[] {((int) (origin_x + width)), ((int) origin_y), ((int) (origin_x + width)), ((int) (origin_y + height)), color_r, color_g, color_b});
            //vertex_arrayCD.setData(new int[] {((int) (origin_x + width)), ((int) (origin_y + height)), ((int) (origin_x / origin_z)), ((int) ((origin_y + height) / origin_z)), color_r, color_g, color_b});
            //vertex_arrayDA.setData(new int[] {((int) (origin_x / origin_z)), ((int) ((origin_y + height) / origin_z)), ((int) (origin_x / origin_z)), ((int) (origin_y / origin_z)), color_r, color_g, color_b});
            vertex_array.setProgram(shaderProgram);
            vertex_array.setData(makeVertexData());
        }
	}

	/**
	 * Sets the color of the rectangle
	 * @param R Red channel value between 0 and 255
	 * @param G Green channel value between 0 and 255
	 * @param B Blue channel value between 0 and 255
	 */
	public void setColor(int R, int G, int B) {
		color_r = R;
		color_g = G;
		color_b = B;
	}

	public void setColor(Color color) {
		color_r = color.getRed();
		color_g = color.getGreen();
		color_b = color.getBlue();
	}


	/**
	 * Set the X physics velocity of the object
	 * @param magnitude
	 */
	public void setPhysics_velocity_x(int magnitude){
		physics_velocity_x = magnitude;
	}

	/**
	 * Set the Y physics velocity of the object
	 * @param magnitude
	 */

	public void setPhysics_velocity_y(int magnitude){
		physics_velocity_y = magnitude;
	}

	/**
	 * Set the X and Y physics velocity of the object
	 * @param x
	 * @param y
	 */
	public void setPhysics_velocity(int x, int y){
		physics_velocity_x = x;
		physics_velocity_y = y;
	}

	/**
	 * Get the physics velocity of the object
	 * @return int[2] of physics velocity
	 */
	public double[] getPhysics_velocity(){
		return new double[] {physics_velocity_x, physics_velocity_y};
	}


	/**
	 * Set the object's position
	 * @param x
	 * @param y
	 */
	public void setpos(double x, double y) {
		origin_x = x;
		origin_y = y;
		//origin_z = z;
	}

	/**
	 * Move the object by an offset
	 * @param x Velocity along the x axis
	 * @param y Velocity along the y axis
	 */
	public void move(double x, double y) {
		origin_x = origin_x + x;
		origin_y = origin_y + y;
	}

	/**
	 * Move the object by the distance it would travel in period {@code time} at its current velocity
	 * @param time Scale the distance by this factor
	 */
	public void move(double time){
		setpos(getpos()[0] + (physics_velocity_x * time), getpos()[1] + (physics_velocity_y * time));
	}



	/**
	 * Get the object's position
	 * @return int[2] of {@code {X, Y}}
	 */
	public double[] getpos() {
		return new double[]{ origin_x, origin_y };

	}

	public void getSizes() {
		double[] tout = { origin_x, origin_y, height, width, color_r, color_g, color_b };
		//System.out.print("sizes are: ");
		//System.out.println(tout[0] + " " + tout[1] + " " + tout[2] + " " + tout[3]);

	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	/**
	 * Set the X position of the center of the object
	 * @param x X value for the center of the object
	 */
	public void centerx(int x) {
		origin_x = x - (width / 2);
	}

	/**
	 * Get the X position of the center of the object
	 * @return
	 */
	public double centerx(){
		return origin_x + (width / 2);
	}

	/**
	 * Get the Y position of the center of the object
	 * @return
	 */
	public double centery(){
		return origin_y + (height / 2);
	}

	/**
	 * Set the Y position of the center of the object
	 * @param y Y value for the center of the object
	 */
	public void centery(int y) {
		origin_y = y - (height / 2);
	}

	public void setWidth(int value) {
		width = value;
	}

	public void setHeight(int value) {
		height = value;
	}

	public void enable(){
		this.enabled = true;
	}

	public void disable(){
		this.enabled = false;
	}

	public void toggle() {
        this.enabled = !enabled;
	}

	/**
	 * Bind an animation to this target. Binding occurs in the animation, but the binding action can be called from the Rect as well.
	 * @param anim
	 * @return 1 if sucessful
	 */
	public int bind_Animation(Animation anim){
		self_animation = anim;
		self_animation.setTarget(this);
		return 1;
	}

	/**
	 * Sets the object to act as though it were a Sprite
	 * @deprecated
	 */
	public void make_Sprite(){
		sprite = true;
	}

	/**
	 * Step a sprite animation
	 * @deprecated
	 * @param stepby
	 * @return
	 */
	public int sprite_Step(int stepby){
		sprite_step = sprite_step + 1;
		return sprite_step;
	}

	/**
	 * Reset a sprite animation
	 * @deprecated
	 */
	public void reset_sprite_animation(){
		sprite_step = 0;
	}

	/**
	 * Get the sprite animation's step
	 * @deprecated
	 * @return
	 */
	public int get_step(){
		return sprite_step;
	}


	/**
	 * Give the object a mnemonic name.
	 * @param identifier A name that the object can be idintified by
	 */
	public void setLabel(String identifier){
		label = identifier;
	}

	public void applyForce(double x, double y) {
		physics_velocity_y = physics_velocity_y + y;
		physics_velocity_x = physics_velocity_x + x;
	}

	private int[] makeVertexData() {
	    return new int[] {roundAndCast(origin_x), roundAndCast(origin_y), roundAndCast(origin_z), roundAndCast(height), roundAndCast(width), color_r, color_g, color_b};
    }

    public void TEMP_setZ(double z) {
		origin_z = z;
	}


}
