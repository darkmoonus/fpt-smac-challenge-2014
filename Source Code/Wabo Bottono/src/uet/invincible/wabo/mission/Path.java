package uet.invincible.wabo.mission;

import java.util.ArrayList;

import com.fpt.robot.types.RobotPose2D;

public class Path {
	public ArrayList<RobotPose2D> listPoint = new ArrayList<RobotPose2D>();
	public int targetid;
	public String action = "put";
	
	public Path(ArrayList<RobotPose2D> listPoint, int targetid, String action) {
		super();
		this.listPoint = listPoint;
		this.targetid = targetid;
		this.action = action;
	}
	public ArrayList<RobotPose2D> getListPoint() {
		return listPoint;
	}
	public void setListPoint(ArrayList<RobotPose2D> listPoint) {
		this.listPoint = listPoint;
	}
	public int getTargetid() {
		return targetid;
	}
	public void setTargetid(int targetid) {
		this.targetid = targetid;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
