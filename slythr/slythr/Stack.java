package slythr;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Stacks are responsible for storage and management of Primitives. Primitives are added to the stack, and can then be drawn
 * with the draw() method.
 */
public class Stack implements Iterable<Primitive>{

	/**
	 * An {@code ArrayList} containing the stack's primitives.
	 */
	CopyOnWriteArrayList<Primitive> stack = new CopyOnWriteArrayList<>();
	CopyOnWriteArrayList<Primitive> top_stack = new CopyOnWriteArrayList<>();


	/**
	 * Instantiates a new Stack.
	 */
	public Stack() {
		// java.util.List = new java.util.ArrayList<Object>()

		// System.out.println("stack has been created!");
	}

	/**
	 * Add a primitive to the stack.
	 *
	 * @param obj a Primitive to be added to the stack
	 */
	public void add(Primitive obj) {
		stack.add(obj);

	}

	/**
	 * Prints the objects in the stack
	 */
	public void printMe() {
		for (Primitive tout : stack) {
			System.out.println(tout);
		}

	}

	/**
	 * Disable all items in the stack
	 */
	public void disable_all(){
		for (Primitive primitive : stack) {
			primitive.disable();
		}
	}

	/**
	 * Len int.
	 *
	 * @return the number of items in the stack.
	 */
	public int len() {
		return stack.size();
	}

	/**
	 * Gets a primitive from the stack.
	 *
	 * @param index the index of the Primitive within the stack
	 * @return the target Primitive.
	 */
	public Object get(int index) {
		return stack.get(index);
	}

	/**
	 * Draw all enabled Primitives within the stack to the target {@code Graphics} instance g.
	 *
	 * @param g instance of {@code Graphics} on which to draw.
	 */
	synchronized public void draw(Graphics g) {
		for (Primitive obj : stack) {
			if (!obj.draw_on_top) {
				try {
					obj.draw(g);
				} catch (java.util.ConcurrentModificationException e) {
					//pass; suppresses error message
				}
			} else {
				top_stack.add(obj);
			}
		}
		for (Primitive primitive : top_stack) {
			try {
				primitive.draw(g);
			} catch (java.util.ConcurrentModificationException e) {
				//pass;; suppresses error message
			}
		}
	}

	/**
	 * Remove an object from the stack.
	 *
	 * @param obj the obj to be removed from the stack
	 */
	public void remove(Primitive obj) {
	    try {
            stack.remove(stack.indexOf(obj));
        } catch (ArrayIndexOutOfBoundsException e) {
	    	e.printStackTrace();
	        //System.out.println("Failed to remove item from stack: java.lang.ArrayIndexOutOfBoundsException << error handled sucessfully");
        }

        //handles a rarely-ocurring error involving out of bounds exceptions when trying to kill an object -- may result from frame
        //descrepencies and update speeds resulting in multiple attempts to remove an object -- shouldnt produce static semantics error, but keep an eye out
	}

	/**
	 * Make a new stack instance containing the {@code Primmitive}s in an ArrayList
	 *
	 * @param list the list of objects to be added to the new stack.
	 * @return the new stack
	 */
	public Stack makeStack(ArrayList<Primitive> list) {
		//call <target> = makeStack(<origin_list>) to effectively convert a list into a stack and store at target
		Stack newStack = new Stack();
		for (Primitive obj : list) {
			newStack.add(obj);
		}
		return newStack;
	}

	/**
	 * Clear all objects from the stack.
	 */
	public void flush(){
//		for (Primitive stackitem : stack){
//			stack.remove(stack.indexOf(stackitem));
//		}
		stack.clear();
	}

	/**
	 * Add the items in a stack to the stack.
	 *
	 * @param addme the stack of {@code Primitive}s to be added to the stack.
	 */
	public void add(Stack addme){
		for (Primitive obj : addme){
			stack.add(obj);
		}
	}

	//public void add(ArrayList<Primitive> addme)

	/**
	 * Make an array list of the objects in the stack
	 *
	 * @return an array list of the objects in the stack
	 */
	public ArrayList<Primitive> makeArrayList(){
		ArrayList<Primitive> tout = new ArrayList<>();
		for (Primitive obj : stack){
			tout.add(obj);
		}
		return tout;
	}

	public Stack dump() {
		Stack temp_stack = new Stack();
		for (Primitive primitive : stack) {
			temp_stack.add(primitive);
		}
		stack.clear();
		return temp_stack;
	}

	/**
	 * Return an array {@code Object[]} of the objects in the stack.
	 *
	 * @return an array {@code Object[]} of the objects in the stack
	 */
	public Object[] toArray(){
		return stack.toArray();
	}

	public void enable() {
		for (Primitive primitive : stack) {
			primitive.enable();
		}
	}

	public void disable() {
		for (Primitive primitive : stack) {
			primitive.disable();
		}
	}

	@Override
	public Iterator<Primitive> iterator() {
		return stack.iterator();
	}
}
