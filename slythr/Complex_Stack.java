package slythr;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is responsible for managing groups of complexes. It is analogous to the stack class, which is responsible for primitives.
 */

public class Complex_Stack {

	public CopyOnWriteArrayList<Complex> stack = new CopyOnWriteArrayList<>();


	/**
	 * Create a new complex_stack.
	 */
	public Complex_Stack() {
		// java.util.List = new java.util.ArrayList<Object>()

		// System.out.println("stack has been created!");
	}

	/**
	 * Add a complex to the stack.
	 * @param obj object to add to the stack
	 */
	public void add(Complex obj) {
		stack.add(obj);

	}

	/**
	 * Print the contents of the stack.
	 */
	public void printMe() {
		for (Complex tout : stack) {
			System.out.println(tout);
		}

	}

	/**
	 * Get the size of the stack.
	 * @return an integer of the number of items in the stack
	 */
	public int len() {
		return stack.size();
	}

	public Object get(int index) {
		return stack.get(index);
	}

	public void draw(Graphics g) {
		for (Complex obj : stack) {
			obj.draw(g);

		}
	}

	/**
	 * Remove an item from the stack.
	 * @param obj the item to remove
	 */
	public void remove(Complex obj) {
	    try {
            stack.remove(stack.indexOf(obj));
        } catch (ArrayIndexOutOfBoundsException e) {
	        System.out.println("Failed to remove item from stack: java.lang.ArrayIndexOutOfBoundsException << error handled sucessfully");
        }

        //handles a rarely-ocurring error involving out of bounds exceptions when trying to kill an object -- may result from frame
        //descrepencies and update speeds resulting in multiple attempts to remove an object -- shouldnt produce static semantics error, but keep an eye out
	}

	/**
	 * Make a stack from a list, and return it.
	 * @param list list of items to add to the new stack
	 * @return the new stack.
	 */
	public Complex_Stack makeStack(List<Complex> list) {
		//call <target> = makeStack(<origin_list>) to effectively convert a list into a stack and store at target
		Complex_Stack newStack = new Complex_Stack();
		for (Complex obj : list) {
			newStack.add(obj);
		}
		return newStack;
	}

	/**
	 * Clear all items from the stack.
	 */
	public void flush(){
//		for (Primitive stackitem : stack){
//			stack.remove(stack.indexOf(stackitem));
//		}
		stack.clear();
	}

	/**
	 * Add the items in a stack to the stack.
	 * @param addme stack of items to add.
	 */
	public void add(Complex_Stack addme){
		for (Complex obj : addme.stack){
			stack.add(obj);
		}
	}

	//public void add(ArrayList<Primitive> addme)

//	public ArrayList<Primitive> makeArrayList(){
//		ArrayList<Primitive> tout = new ArrayList<>();
//		for (Complex obj : stack){
//			tout.add(obj);
//		}
//		return tout;
}

	//public Stack dump(){
        //empty for now, should return the stack (or an ArrayList?) and then clear it...will need to use temporary storage
        //buffer (i.e. store stack in temp, clear stack, return temp
//	public Object[] toArray(){
//		return stack.toArray();
//	}

	//}



