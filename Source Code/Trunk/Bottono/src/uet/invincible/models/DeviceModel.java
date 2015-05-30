package uet.invincible.models;

import uet.invincible.myobjects.Dimension;

public class DeviceModel {
	protected Dimension resolution;
	protected String name;
	protected boolean isOrientation;
	protected boolean isTablet;
	public DeviceModel() {
		setResolution(new Dimension(0, 0));
		setName("Device");
		setOrientation(false);
		setTablet(false);
	}
	public DeviceModel(Dimension resolution, String name, boolean isOrientation, boolean isTablet) {
		setResolution(resolution);
		setName(name);
		setOrientation(isOrientation);
		setTablet(isTablet);
	}
	public DeviceModel(Dimension resolution, String name) {
		setResolution(resolution);
		setName(name);
	}
	public String toString() {
		String s = getName() + ": (" + getResolution().getWidth() + ", "
				+ getResolution().getHeight() + ") " + "Orientation: "
				+ isOrientation + " Tablet: " + isTablet;
		return s;
	}
	public Dimension getResolution() {
		return resolution;
	}
	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOrientation() {
		return isOrientation;
	}
	public void setOrientation(boolean isOrientation) {
		this.isOrientation = isOrientation;
	}
	public boolean isTablet() {
		return isTablet;
	}
	public void setTablet(boolean isTablet) {
		this.isTablet = isTablet;
	}
	
}
