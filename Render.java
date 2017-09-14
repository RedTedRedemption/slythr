package slythr;

import java.awt.*;

public class Render extends Thread {

	private Thread t;
	private String threadName;
	private Graphics g;
	Stack renderStack;
	public boolean stack_bound = true;
	public boolean graphics_bound = true;
	public boolean alive = true;
	public boolean enabled = true;

	public Render(String name, Graphics G, Stack stack){
		threadName = name;
		g = G;
		renderStack = stack;
		System.out.println("created render instance");
	}

	public void run(){
		System.out.println("render cycle starting");
		if (stack_bound && graphics_bound) {
			System.out.println("rendering");
			renderStack.draw(g);
			System.out.println("render has finished");
		}
		if (!stack_bound) {
			System.out.println("cant render yet, there is no bound stack");
		}
		if (!graphics_bound) {
			System.out.println("cant render yet, there is no graphics object bound");
		}
		System.out.println("render cycle complete");
	}

//
//	public void bindStack(Stack tobind){
//		System.out.print("binding stack...");
//		renderStack = tobind;
//		stack_bound = true;
//		System.out.println("done");
//	}
//
//	public void bindGraphics(Graphics G){
//		System.out.print("binding graphics object...");
//		g = G;
//		graphics_bound = true;
//		System.out.println("done");
//	}

//	public void start(Stack renderStack){
//		if (t == null){
//			System.out.print("creating render thread...");
//			t = new Thread(this, threadName);
//			System.out.println("done");
//			System.out.print("starting render thread...");
//			t.start();
//			System.out.println("done");
//		}
//	}
}
