package uet.invincible.wabo.mission;

import java.util.ArrayList;

import com.fpt.robot.types.RobotPose2D;

public class WaboState {
	public static String token = "";
	public static RobotPose2D currentPos = new RobotPose2D();
	public static Order currentOrder;
	public static Path currentPath;
	public static int startPoint = 1;
	
	public static ArrayList<Order> ordersList = new ArrayList<Order>();
	public WaboState() {
		
	}
	
	
	public static ArrayList<String> tableName = new ArrayList<String>();
}
