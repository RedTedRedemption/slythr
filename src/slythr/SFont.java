package slythr;

import java.awt.*;

public class SFont extends Resource {
	int style;
	String name;


	public SFont(int Style, String Name) {

		style = Font.PLAIN;
		name = Name;

	}

	public void setAttributes(int Style, String Name) {

		style = Style;
		name = Name;

	}

	public Font getFont(int size) {

		return new Font(name, style, size);

	}

}
