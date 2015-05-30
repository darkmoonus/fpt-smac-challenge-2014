package uet.invincible.wabo.mission;

import java.util.ArrayList;

import com.fpt.robot.types.RobotPose2D;

public class PathToBar {
	
	public static boolean inside_section(RobotPose2D point, float x1, float x2, float y1, float y2) {
    	if (point.x > x2 || point.x < x1 || point.y < y1 || point.y > y2){
    		return false;
    	}    		
    	return true;
    }
	
	public static Path pathToBar(RobotPose2D from, RobotPose2D to) {
		ArrayList<RobotPose2D> path = new ArrayList<RobotPose2D>();
        int targetId = 1;
        String action = "get";
        Path result = new Path(path, targetId, action);
        
        //Find path to get food
        RobotPose2D toFood = new RobotPose2D();
        if(to.equals(WaboAlgorithm.dishesTableCenter1) || to.equals(WaboAlgorithm.dishesTableCenter2)) {
        	toFood = WaboAlgorithm.foodPosition.get(WaboState.currentOrder.foodId+4);
        }
        
        if(to.equals(WaboAlgorithm.dishesTableLeft)) {
        	toFood = WaboAlgorithm.foodPosition.get(WaboState.currentOrder.foodId-1);
        }
        
        if(to.equals(WaboAlgorithm.dishesTableRight)) {
        	toFood = WaboAlgorithm.foodPosition.get(WaboState.currentOrder.foodId+9);
        }
        
        // Find path back to bar left
        if(to.equals(WaboAlgorithm.dishesTableLeft)) {
        	if(inside_section(from, -3, 3, -3, -2)) {
        		path.add(WaboAlgorithm.bottomLeft);        		   		
        	} else
        	if (inside_section(from, -1, 1, -2, -1)) {
        		path.add(WaboAlgorithm.startPoint1);
        		path.add(WaboAlgorithm.bottomLeft);
        	} else
        	if (inside_section(from, -2, -1, -1, 0)) {
            	path.add(WaboAlgorithm.leftPoint);
            	path.add(WaboAlgorithm.startPoint1);
            	path.add(WaboAlgorithm.bottomLeft);
            }        	
        }
        
        //Find path back to bar center
        if(to.equals(WaboAlgorithm.dishesTableLeft)) {
        	if(inside_section(from, 0, 2, -1, 1)) {
        		path.add(WaboAlgorithm.dishesTableCenter1);        		   		
        	} else
        	if (inside_section(from, -1, 1, -2, -1)) {
        		path.add(WaboAlgorithm.table1Left);
        		path.add(WaboAlgorithm.dishesTableCenter1);
        	} else
        	if (inside_section(from, -1, 1, 1, 2)) {
            	path.add(WaboAlgorithm.table1Right);
            	path.add(WaboAlgorithm.dishesTableCenter2);
            } else
            if (inside_section(from, -2, -1, -1, 0)) {
               	path.add(WaboAlgorithm.leftPoint);
               	path.add(WaboAlgorithm.table1Left);
              	path.add(WaboAlgorithm.dishesTableCenter1);
            } else
         	if (inside_section(from, -2, -1, 0, 1)) {
               	path.add(WaboAlgorithm.rightPoint);
               	path.add(WaboAlgorithm.table1Right);
               	path.add(WaboAlgorithm.dishesTableCenter2);
            }
            	
        }
        
        //Find path back to bar right
        if(to.equals(WaboAlgorithm.dishesTableRight)) {
        	if(inside_section(from, -3, 3, 2, 3)) {
        		path.add(WaboAlgorithm.bottomRight);        		   		
        	} else
        	if (inside_section(from, -1, 1, 1, 2)) {
        		path.add(WaboAlgorithm.startPoint2);
        		path.add(WaboAlgorithm.bottomRight);
        	} else
           	if (inside_section(from, -2, -1, 0, 1)) {
               	path.add(WaboAlgorithm.rightPoint);
               	path.add(WaboAlgorithm.startPoint2);
               	path.add(WaboAlgorithm.bottomRight);
            }
        }
        
        path.add(toFood);
        result.setListPoint(path);        
        return result;
	}
}
