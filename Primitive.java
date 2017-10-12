package slythr;

import java.awt.*;

/**
 * A super class for primitives. Does not do anything on its own, but allows multiple types of primitives to be manipulated
 * as though they were the same type.
 */

public class Primitive {

	public double[] physics_velocity;
	public double height;
	public double width;
	public double origin_x;
	public double origin_y;
	public double[] origin;
	public double center_x;
	public double center_y;
	public double self_size;
	public double physics_velocity_x;
	public double physics_velocity_y;
	public boolean enabled = true;
	public Font use_font;
	public Animation self_animation;
	public boolean sprite;
	public int sprite_step;
	public Primitive bounding_box;
	public String type;
	public String label;
	public double scale;
	public boolean draw_on_top = false;

	// primitives

	public void update(Graphics g){

	}

	public void setOpacity(int opacity){

	}

	public void draw(Graphics g) {
		System.out.println("drawing from primitive");
	}

	public void setpos(double x, double y) {
		System.out.println("setting position from primitive");
	}

	public double[] getpos() {
		return new double[]{ 0, 0 };

	}

	public void setImage(String path) throws java.io.IOException {

	}

	public void setAttributes(double x, double y, double Height, double Width, int r, int g, int b) {
		// sets attributes of primitive object
	}

	public void centerx(int x) {
		// sets origin to align center x to this coordinate

	}
	public void centerx(int x, Graphics g) {
	}

	public double centerx(){
		return 1;
	}


	public void centery(int y) {
		// sets origin to align center y to this coordinate
	}

	public double centery(){
		return 1;
	}

	public void setSFont(Resource hellofont) {

	}

	public double[] getPhysics_velocity(){
		//return physics_velocity;
		return physics_velocity;
	}

	public void move(int x, int y) {
		// moves the object by its physics_velocity
	}

	public void setVelocity(int[] vel) {
		// sets an object's velocity in its attribute physics_velocity
	}

	public void updatePoints() {

	}

	public void setWidth(int width) {
		// TODO Auto-generated method stub
	}

	public void setHeight(int i) {

	}

	public void setRotation(double i) {

	}

	public void setText(String content) {

	}

	public void getSizes() {

	}

	public double getHeight(){
		return -1;
	}

	public double getWidth() {
		return -1;
	}

	public void triAttributes(int h, int w, int x, int y) {

	}

	public void setColor(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	public void setSize(int size){

	}

	public int getSize(){
		return 1;
	}

	public String getSelfContent() {
		return "error, wrong scope for getSelfContent call";
	}

	public void move(double time){
		//setpos(getpos()[0] + physics_velocity_x, getpos()[1] + physics_velocity_y);

	}

	public void setPhysics_velocity_x(int magnitude){
		//physics_velocity_x = magnitude;
	}

	public void setPhysics_velocity_y(int magnitude){
	//	physics_velocity_y = magnitude;
	}

	public void setPhysics_velocity(int x, int y){
		//physics_velocity_x = x;
		//physics_velocity_y = y;
	}



	public void enable(){}
	public void disable(){

	}
	public void toggle() {
	}

//	public void update(){
//
//	}

	public boolean isClicked(){
		return false;
	}

	public int bind_Animation(Animation anim){
		return -1;
	}

	public void make_Sprite(){
		sprite = true;
	}

	public int sprite_Step(int stepby){
		return -1;
	}

	public int getSprite_step(){
		return -1;
	}
	public int get_step(){
		return -1;
	}

	public void reset_sprite_animation(){
        //noinspection UnnecessaryReturnStatement
        return;
	}

	public Primitive getBounding_box(){
		try {
			throw new SlythrError("attempted to call method getBounding_box() from parent class, could be an error in SLYTHR");

		} catch (SlythrError slythrError) {
			slythrError.printStackTrace();
			return new Primitive();
		}
	}

	public void draw_bounding_box(Graphics g){

	}

	public void setLabel(String identifier){
		label = identifier;
	}


	public void setScaleMode(int mode) {
	}

	public void setScale(double Scale) {
	}

	public void setFitHeight(int Height) {
	}

	public void setFitWidth(int Width) {
	}

	public void move_to_top(){
		draw_on_top = true;
	}

	public void applyForce(double x, double y) {

	}

	public void setEndpoint_x(int x) {
		try {
			throw new SlythrError("attempted to call method setEndpoint_x() from parent class, could be an error in SLYTHR");
		} catch (SlythrError slythrError) {
			slythrError.printStackTrace();
		}
	}

	public void setEndpoint_y(int y) {
		try {
			throw new SlythrError("attempted to call method setEndpoint_y() from parent class, could be an error in SLYTHR");
		} catch (SlythrError slythrError) {
			slythrError.printStackTrace();
		}
	}

	public void setEndpoint(int x, int y) {
		try {
			throw new SlythrError("attempted to call method setEndpoint() from parent class, could be an error in SLYTHR");
		} catch (SlythrError slythrError) {
			slythrError.printStackTrace();
		}
	}

	public int roundAndCast(double d) {
		return ((int) Math.round(d));
	}
}
