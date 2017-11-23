package slythr;

import java.awt.*;

public class Resource {

	public Resource self_font;

	public void setAttributes(int Size, int Style, String Name) {
		// sets attributes
	}

	public void setSize() {
		// parent for setting size
	}

	public Font getFont(int size) {
		// returns the font for usage
		Font empty = new Font("Serif", Font.ITALIC, size); // this is an empty
															// font that we
		// don't care about, its just to make the function return a Font type
		return empty;
	}

}
