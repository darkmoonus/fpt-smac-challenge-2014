package uet.invincible.wabo.mission;

import java.util.ArrayList;

import com.fpt.robot.types.RobotPose2D;

public class PathToTable {
	
	public static boolean inside_section(RobotPose2D point, float x1, float x2, float y1, float y2) {
    	if (point.x > x2 || point.x < x1 || point.y < y1 || point.y > y2){
    		return false;
    	}    		
    	return true;
    }
	
	public static Path pathToTable(RobotPose2D from, RobotPose2D to) {
		ArrayList<RobotPose2D> path = new ArrayList<RobotPose2D>();
        int targetId = 1;
        String action = "put";
        Path result = new Path(path, targetId, action);
        
        //If to is bar 2 or 5
        if(to.equals(WaboAlgorithm.table2)) {
        	path.add(WaboAlgorithm.table2Front);
    		result.setListPoint(path);
    		return result;
        }
        
        if(to.equals(WaboAlgorithm.table5)) {
    		path.add(WaboAlgorithm.table5Front);
    		result.setListPoint(path);
    		return result;
    	}    
        
        //From bar left      
        if(inside_section(from, 2, 3, -3, -1)){        	    	        
        	if(to.equals(WaboAlgorithm.table1)) {
        		path.add(WaboAlgorithm.dishesTableCenter2);
        		path.add(WaboAlgorithm.table1Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table4)) {
        		path.add(WaboAlgorithm.bottomLeft);
        		path.add(WaboAlgorithm.table4Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table3)) {
        		path.add(WaboAlgorithm.dishesTableCenter1);
        		path.add(WaboAlgorithm.table3Right);
        		result.setListPoint(path);
        		return result;
        	}
        }
        
        //From bar center
        if(inside_section(from, 2, 3, -1, 1)){        	
        	if(to.equals(WaboAlgorithm.table1)) {        		
        		path.add(WaboAlgorithm.table1Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table4)) {
        		path.add(WaboAlgorithm.bottomLeft);
        		path.add(WaboAlgorithm.table4Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table3)) {
        		path.add(WaboAlgorithm.bottomRight);
        		path.add(WaboAlgorithm.table3Right);
        		result.setListPoint(path);
        		return result;
        	}
        }
        
        //From bar right
        if(inside_section(from, 2, 3, 1, 3)){        	
        	if(to.equals(WaboAlgorithm.table1)) {
        		path.add(WaboAlgorithm.dishesTableCenter2);
        		path.add(WaboAlgorithm.table1Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table4)) {
        		path.add(WaboAlgorithm.bottomLeft);
        		path.add(WaboAlgorithm.table4Left);
        		result.setListPoint(path);
        		return result;
        	}
        	if(to.equals(WaboAlgorithm.table3)) {
        		path.add(WaboAlgorithm.bottomRight);
        		path.add(WaboAlgorithm.table3Right);
        		result.setListPoint(path);
        		return result;
        	}
        }
        
        return result;
	}

}
