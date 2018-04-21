package slythr;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
	public ShaderProgram shaderProgram;
	public static ShaderProgram plainShaderProgram;
	public Vertex_Array vertex_array;
	public ArrayList<Property> properties = new ArrayList<>();
	public boolean needsNotify_Key = false;
	public SPassAction<KeyEvent> onEventAction_Key;

	public void setOnEvent_Key(SPassAction<KeyEvent> action) {
		onEventAction_Key = action;
		Engine.notifyStack_Key.add(this);
	}

	public void removeOnEvent_Key() {
		Engine.notifyStack_Key.remove(this);
		onEventAction_Key = null;
	}

	// primitives

	public void update(Graphics g){
	}

	public Primitive dupe() {
		try {
			return (Primitive) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("ERROR: FAILED TO CLONE PRIMITIVE OBJECT!");
			return null;
		}
	}

	public void eventNotify_Key(KeyEvent e) {
		onEventAction_Key.action(e);
	}

	public void setOpacity(int opacity){

	}

	public void draw(Graphics g) {
		System.out.println("drawing from primitive");
	}

	public void setpos(double x, double y) {
        Engine.throwFatalError(new SlythrError("ERROR: Attempted to call method setpos() from parent class, could be an error in SLYTHR"));

    }

//    public void setpos(double x, double y, double z) {
//		Engine.throwFatalError(new SlythrError("ERROR: Attempted to call method setpos(x,y,z) from parent class, could be an error in SLYTHR"));
//	}

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

	public void move(double x, double y) {
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

	public void setColor(int[] colorArray) {
		Engine.throwFatalError(new SlythrError("ERROR: attempted to call method setColor() from parent class, could be an error in SLYTHR"));
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

	public void setPhysics_velocity_x(double magnitude){
		//physics_velocity_x = magnitude;
	}

	public void setPhysics_velocity_y(double magnitude){
	//	physics_velocity_y = magnitude;
	}

	public void setPhysics_velocity(double x, double y){
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
	    Engine.throwFatalError(new SlythrError("ERROR: attempted to call method getBounding_box() from parent class, could be an error in SLYTHR"));
		return null;
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
		Engine.throwFatalError(new SlythrError("attempted to call method setEndpoint_x() from parent class, could be an error in SLYTHR"));
	}

	public void setEndpoint_y(int y) {
	    Engine.throwFatalError(new SlythrError("attempted to call method setEndpoint_y() from parent class, could be an error in SLYTHR"));
	}

	public void setEndpoint(int x, int y) {
	    Engine.throwFatalError(new SlythrError("attempted to call method setEndpoint() from parent class, could be an error in SLYTHR"));
	}

	public void TEMP_setZ(double z) {
		Engine.throwFatalError(new SlythrError("attempted to call method TEMP_setZ() from parent class, could be an error in SLYTHR"));
	}

	public static int roundAndCast(double d) {
		return ((int) Math.round(d));
	}

	public void setShaderProgram(ShaderProgram ShaderProgram) {
		shaderProgram = ShaderProgram;
	}

	public void addProperty(Property property) {
		properties.add(property);
	}

	public Primitive newMe() {
		Engine.throwFatalError(new SlythrError("attempted to call method newMe() from parent class, could be an error in SLYTHR"));
		return null;
	}

	public Primitive newMe(boolean b) {
		Engine.throwFatalError(new SlythrError("attempted to call method newMe() from parent class, could be an error in SLYTHR"));
		return null;
	}

}
