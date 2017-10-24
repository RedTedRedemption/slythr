package slythr;

import java.awt.*;

/**
 * Class for simulation of various physics related functions such as gravity (not implemented yet) and object collision
 */

public class Physics {


	static long physstart = 0;

	public static long simfrom = 1;

	public static long elapsed;

	// public boolean objectsCollide(slythr.Primitive obj1, slythr.Primitive obj2){
	// System.out.println("checking collison");
	// boolean collides;
	//
	// for (int vertrays = 0; vertrays <= obj1.height; vertrays = vertrays + 1){
	// collides = castRay(obj1.origin_x, (obj1.origin_y + vertrays), 1, obj2);
	// //PROBLEM IS PROBABLY HERE!!!!
	// System.out.println(obj1 + " and " + obj2 + "'s collision state is " +
	// collides);
	// if (collides){
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * Casts a ray
	 * @deprecated
	 * @param startx
	 * @param starty
	 * @param endx
	 * @param target
	 * @return
	 */
	public boolean castRay(int startx, int starty, int endx, Primitive target) {
		System.out.println("casting ray");
		double targtop = getPointsRect(target)[1];
		double targbot = getPointsRect(target)[5];
		double targleft = getPointsRect(target)[0];
		double targright = getPointsRect(target)[3];
		if (starty <= targbot && starty >= targtop) {
			for (int i = startx; i <= endx; i = i + 1) {
				System.out.println("casting ray at point offset " + i);
				if (i >= targleft && i <= targright) {
					System.out.println("ray collides!");
					return true;
				}
			}
		}
		System.out.println("ray does not collide");
		return false;
	}

	public static double[] getPointsRect(Primitive obj) {
		// [0, 1] [2, 3]
		// A----------B
		// |          |
		// |          | <== REMEMBER, Y VALUE IS INVERTED!
		// |          |
		// C----------D
		// [4,5] [6, 7]

		double ax = obj.getpos()[0];
		// System.out.println(ax);
		double ay = obj.getpos()[1];
		// System.out.println(ay);

		double bx = ax + obj.getWidth();
		// System.out.println(bx);
		// System.out.println(by);

		// System.out.println(cx);
		double cy = ay + obj.getHeight();
		// System.out.println(cy);

		double dx = ax + obj.getWidth();
		double dy = ay + obj.getHeight();

		// System.out.println("retrived rect points: " + ax + " " + ay + " " +
		// bx);
		// System.out.println(tout);
		return new double[]{ ax, ay, bx, ay, ax, cy, dx, dy };
	}

	public static boolean pointInObj(double x, double y, Primitive obj) {
		// System.out.println("testing for point within object...");
		double[] point_arr = getPointsRect(obj);
		// System.out.println("Sample of point array: " + point_arr[0] + " " +
		// point_arr[1]);
        // System.out.println("point is in object");
		// System.out.println("point is not in object");
        return x > point_arr[0] && x < point_arr[2] && y > point_arr[1] && y < point_arr[5];
	}

	public static boolean doObjectsCollide(Primitive obj1, Primitive obj2) {
//		obj1.update();
//		obj2.update();
		// System.out.println("Starting collision test");
		double[] point_arr1 = getPointsRect(obj1);
		double[] point_arr2 = getPointsRect(obj2);
		// System.out.println("collision test returning true");
		return pointInObj(point_arr1[0], point_arr1[1], obj2) || pointInObj(point_arr1[0], point_arr1[5], obj2) || pointInObj(point_arr1[2], point_arr1[1], obj2) || pointInObj(point_arr1[2], point_arr1[5], obj2) || pointInObj(point_arr2[0], point_arr2[1], obj1) || pointInObj(point_arr2[0], point_arr2[5], obj1) || pointInObj(point_arr2[2], point_arr2[1], obj1) || pointInObj(point_arr2[2], point_arr2[5], obj1);
// System.out.print("collision test returning true");

	}

	public static void simulate(int time, Graphics g){
		//simfrom = 1 + Instant.now().get(ChronoField.INSTANT_SECONDS);
		//physstart = Instant.now().getNano();
		for (Primitive obj : Engine.rendStack.makeArrayList()){
			obj.move(time);

		}
//		elapsed = ((Instant.now().getNano() - physstart) / 1000000) + 1;



		//update text
		for (Primitive obj : Engine.rendStack.makeArrayList()){
			obj.update(g);
		}


	}
}
