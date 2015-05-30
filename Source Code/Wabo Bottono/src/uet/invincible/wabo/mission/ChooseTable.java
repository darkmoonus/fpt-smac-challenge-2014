package uet.invincible.wabo.mission;
import java.util.ArrayList;

import com.fpt.robot.types.RobotPose2D;

public class ChooseTable {
	public static final RobotPose2D startPoint = WaboAlgorithm.startPoint1 ;
	public static final ArrayList<Integer> order1 = new ArrayList<Integer>(){{
		add(2);
		add(1);
		add(3);
		add(5);
		add(4);
	}};
	
	public static final ArrayList<Integer> order2 = new ArrayList<Integer>(){{
		add(5);
		add(1);
		add(4);
		add(2);
		add(3);
	}};
	
	public static final ArrayList<Integer> choose(RobotPose2D a) {				
		
		if(startPoint.equals(WaboAlgorithm.startPoint1)){
			return order1;
		} else
//		for(int i=0; i<=Wabo.target.size(); i++){
//			if()
//		}
		return order2;
	}
}
