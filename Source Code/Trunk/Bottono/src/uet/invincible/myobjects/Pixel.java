package uet.invincible.myobjects;

public class Pixel {
	protected int red;
	protected int blue;
	protected int green;
	public Pixel() {
		setRed(0);
		setBlue(0);
		setGreen(0);
	}
	public Pixel(int red, int green, int blue) {
		setRed(red);
		setBlue(blue);
		setGreen(green);
	}
	public Pixel(Pixel pixel) {
		setRed(pixel.getRed());
		setBlue(pixel.getBlue());
		setGreen(pixel.getGreen());
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	@Override
	public String toString() {
		return "Pixel [red=" + red + ", blue=" + blue + ", green=" + green
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pixel other = (Pixel) obj;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (red != other.red)
			return false;
		return true;
	}
	
}
