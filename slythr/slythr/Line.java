package slythr;

import java.awt.*;

/**
 * Instances of this class render as filled rectangles on the window.
 */


public class Line extends Primitive {

    public int color_r = 255;
    public int color_g = 255;
    public int color_b = 255;
    int center_x;
    int center_y;
    public int origin_x = 0;
    public int origin_y = 0;
    public int endpoint_x = 10;
    public int endpoint_y = 0;
    public int height = 0;
    public int width = 10;
    public int physics_velocity_x = 0;
    public int physics_velocity_y = 0;
    public boolean enabled = true;
    public Animation self_animation;
    public boolean sprite = false;
    public int sprite_step;
    public Primitive bounding_box;
    public String label = "a line object";


    /**
     * Rect constructor.
     */
    public Line() {
        bounding_box = new Rect();
        bounding_box.disable();
        Engine.rendStack.add(this);
    }


    /**
     * Draws the rectangle. Should rarely be called directly. In most cases this method will be called by a {@link Stack Stack}.
     * @param g Graphics instance on which the rect will be drawn.
     */
    synchronized public void draw(Graphics g) {
        // System.out.println("drawing from rect");
        if (enabled) {
            g.setColor(new Color(color_r, color_g, color_b));
            g.drawLine(origin_x, origin_y, endpoint_x, endpoint_y);
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
    public void setpos(int x, int y) {
        origin_x = x;
        origin_y = y;
    }

    /**
     * Move the object by an offset
     * @param x Velocity along the x axis
     * @param y Velocity along the y axis
     */
    public void move(int x, int y) {
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
        int[] tout = { origin_x, origin_y, height, width, color_r, color_g, color_b };
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
        updateMidpoint();
        return origin_x + (width / 2);
    }

    /**
     * Get the Y position of the center of the object
     * @return
     */
    public double centery(){
        updateMidpoint();
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

    public void applyForce(int x, int y) {
        physics_velocity_y = physics_velocity_y + y;
        physics_velocity_x = physics_velocity_x + x;
    }

    public void updateMidpoint() {
        center_x = (origin_x + endpoint_x) / 2;
        center_y = (origin_y + endpoint_y) / 2;

    }

    public void updateHeight_width() {
        width = Math.abs(endpoint_x - origin_x);
        height = Math.abs(endpoint_y - origin_y);
    }

    public Primitive getBounding_box() {
        updateMidpoint();
        updateHeight_width();
        bounding_box.setHeight(height);
        bounding_box.setWidth(width);
        bounding_box.centerx(center_x);
        bounding_box.centery(center_y);
        return bounding_box;
    }

    public void setEndpoint_x(int x) {
        endpoint_x = x;
    }

    public void setEndpoint_y(int y) {
        endpoint_y = y;
    }

    public void setEndpoint(int x, int y) {
        endpoint_x = x;
        endpoint_y = y;
    }

}
