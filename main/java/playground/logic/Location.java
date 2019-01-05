package playground.logic;

public class Location {

	private double x;
	private double y;

	public Location() {
		this.x = 0;
		this.y = 0;
	}

	public Location(String location) { // format "x,y"
		String[] s = location.split(",");
		this.x = new Double(s[0]);
		this.y = new Double(s[1]);
	}

	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Location))
			return false;
		if (((Location) other).x != this.x || ((Location) other).y != this.y)
			return false;
		else
			return true;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}

}
