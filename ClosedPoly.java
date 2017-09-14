package slythr;

import java.awt.Color;
import java.awt.Graphics;

public class ClosedPoly extends Primitive {

	public int height; // distance from center of shortest side to the opposite
						// angle
	public int width; // length of shortest side
	// unimplemented concept for non-isosceles triangles, i.e. equilateral, etc.
	// For now its just isosceles
	// ...'cause im lazy
	// String type;
	public int[] origin = { 60, 60 }; // coordinate point of the center of the
										// triangle: the point bisecting the
										// midline
	//     A
	//    / \
	//   /   \
	//  /     \
	// B-------C
	// letters are coordinates
	public int origin_x;
	public int origin_y;
	public int[] a;
	public int[] b;
	public int[] c;
	public int ax;
	public int ay;
	public int bx;
	public int by;
	public int cx;
	public int cy;
	public int color_r = 255;
	public int color_g = 0;
	public int color_b = 0;
	public double rotation = 0;

	public ClosedPoly() {

	}

	public void triAttributes(int h, int w, int x, int y) {

		height = h;
		width = w;
		origin[0] = x;
		origin_x = x;
		origin[1] = y;
		origin_y = y;
		updatePoints();

	}

	public void updatePoints() { // remember that the y axis is inverted!
		// updates points to positions relative to origin
		origin_x = origin[0];
		origin_y = origin[1];

		ax = origin[0];
		ay = origin[1] - (height / 2);

		bx = origin[0] - (width / 2);
		by = origin[1] + (width / 2);

		cx = origin[0] + (width / 2);
		cy = origin[1] + (width / 2);

		// System.out.println(ax);
		// System.out.println(ay);

		// offset points to account for rotation

		// Double radrot = Math.toRadians(rotation);
		// System.out.println(((Math.cos(radrot) - Math.sin(radrot)) * ax));
		// System.out.println(radrot);
		// System.out.println(ax);
		// System.out.println("-----");

		// qx = ox + math.cos(angle) * (px - ox) - math.sin(angle) * (py - oy);

		// calculation points
		// int calcax = ax - origin[0];
		// int calcay = ay - origin[1];
		// int calcbx = bx - origin[0];
		// int calcby = by - origin[1];
		// int calccx = cx - origin[0];
		// int calccy = cy - origin[1];
		//
		// calcax = (int) (calcax * Math.cos(rotation) - calcay *
		// Math.sin(rotation));
		// calcay = (int) (calcay * Math.cos(rotation) + calcax *
		// Math.sin(rotation));
		//
		// calcbx = (int) (calcbx * Math.cos(rotation) - calcby *
		// Math.sin(rotation));
		// calcby = (int) (calcby * Math.cos(rotation) + calcbx *
		// Math.sin(rotation));
		//
		// calccx = (int) (calccx * Math.cos(rotation) - calcby *
		// Math.sin(rotation));
		// calccy = (int) (calccy * Math.cos(rotation) + calccx *
		// Math.sin(rotation));
		//
		// ax = calcax + origin[0];
		// ay = calcay + origin[1];
		// bx = calcbx + origin[0];
		// by = calcby + origin[1];
		// cx = calccx + origin[0];
		// cy = calccy + origin[1];
		//

		// ax = (int) ((Math.cos(radrot) - Math.sin(radrot)) * ax);
		// ay = (int) ((Math.sin(radrot) + Math.cos(radrot)) * ax);
		// int rotax = ax;
		// int rotay = ay;
		// ax = (int) ((ax * Math.cos(radrot))
		// - (ax * Math.sin(radrot)));
		// System.out.println(ax);
		//
		// ay = (int) ((ay * Math.sin(radrot))
		// + (ay * Math.cos(radrot)));
		// ax = ax + origin[0];
		// ay = ay + origin[1];
		// System.out.println(ay);
		// System.out.println("-----");
		//
		// bx = (int) ((bx * Math.cos(radrot))
		// - (bx * Math.sin(radrot)));
		//
		// by = (int) ((by * Math.sin(radrot))
		// + (by * Math.cos(radrot)));
		//
		// bx = bx + origin[0];
		// by = by + origin[1];
		//
		// cx = (int) ((cx * Math.cos(radrot))
		// - (cx * Math.sin(radrot)));
		//
		// cy = (int) ((cy * Math.sin(radrot))
		// + (cy * Math.cos(radrot)));
		//
		// cx = cx + origin[0];
		// cy = cy + origin[1];
		//

		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(color_r, color_g, color_b));
		g.drawLine(ax, ay, bx, by); // draws a-b
		g.drawLine(bx, by, cx, cy); // draws b-c
		g.drawLine(cx, cy, ax, ay); // draws c-a and completes the triangle!
	}

	public void setRotation(double val) {
		rotation = val;
	}

	public void setHeight(int val) {
		height = val;
	}

	public void setWidth(int val) {
		width = val;
	}

	public void centerx(int x) {
		origin[0] = x;
		updatePoints();
	}

	public void centery(int y) {
		origin[1] = y;
		updatePoints();
	}

	public int[] getpos() {
        return new int[]{ origin[0], origin[1] };
	}

}
