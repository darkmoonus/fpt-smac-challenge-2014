package uet.invincible.wabo.mission;

import java.util.ArrayList;

import uet.invincible.wabo.mission.Path;

import com.fpt.robot.types.RobotPose2D;

/**
 * Created by Hoang on 23/09/2014.
 */

public class WaboAlgorithm {
	
	
    // declare important point
    public static final RobotPose2D startPoint1 = new RobotPose2D(0, -2.5f, 0, true);
    public static final RobotPose2D startPoint2 = new RobotPose2D(0, 2.5f, 0, true);
    public static final RobotPose2D topCenter = new RobotPose2D(-1.5f, 0, 0, true);
    public static final RobotPose2D rightPoint = new RobotPose2D(0, 1.5f, 0, true);
    public static final RobotPose2D leftPoint = new RobotPose2D(0, -1.5f, 0, true);
    public static final RobotPose2D bottomCenter = new RobotPose2D(1.5f, 0, 0, true);
    public static final RobotPose2D bottomLeft = new RobotPose2D(2f, -2.5f, 0, true);
    public static final RobotPose2D bottomRight = new RobotPose2D(2f, 2.5f, 0, true);
    
    public static final float pi = (float)Math.PI;
    
 // declare target point
    public static final RobotPose2D table1 = new RobotPose2D(0, 0, 0, true);
    public static final RobotPose2D table1Left = new RobotPose2D(0.5f, -0.6f, 2*pi/3, true);    
    public static final RobotPose2D table1Right = new RobotPose2D(0.5f, 0.6f, -2*pi/3, true);
    
    public static final RobotPose2D table2 = new RobotPose2D(1.5f, 1.5f, 0, true);
    public static final RobotPose2D table2Left = new RobotPose2D(1.3f, 0.6f, pi/3, true);
    public static final RobotPose2D table2Front = new RobotPose2D(2.5f, 1.5f, pi, true);
    public static final RobotPose2D table2Right = new RobotPose2D(1.3f, 2.4f, -pi/3, true);
    
    public static final RobotPose2D table3 = new RobotPose2D(-1.5f, 1.5f, 0, true);
    public static final RobotPose2D table3Left = new RobotPose2D(-1.1f, 0.8f, 2*pi/3, true);    
    public static final RobotPose2D table3Right = new RobotPose2D(-1.3f, 2.4f, -2*pi/3, true);
    
    public static final RobotPose2D table4 = new RobotPose2D(-1.5f, -1.5f, 0, true);
    public static final RobotPose2D table4Left = new RobotPose2D(-0.9f, -2.4f, 2*pi/3, true);
    public static final RobotPose2D table4Front = new RobotPose2D(-1f, -1.5f, pi, true);
    public static final RobotPose2D table4Right = new RobotPose2D(-1f, -0.8f, -pi/2, true);
    
    public static final RobotPose2D table5 = new RobotPose2D(1.5f, -1.5f, 0, true);
    public static final RobotPose2D table5Left = new RobotPose2D(1.3f, -2.4f, pi/3, true);
    public static final RobotPose2D table5Front = new RobotPose2D(2.5f, -1.5f, pi, true);
    public static final RobotPose2D table5Right = new RobotPose2D(1.3f, -0.6f, -pi/3, true);
    
    public static final RobotPose2D dishesTableLeft = new RobotPose2D(2.2f, -2, 0, true); 
    public static final RobotPose2D Left1 = new RobotPose2D(2.2f, -1.4f, 0, true);
    public static final RobotPose2D Left2 = new RobotPose2D(2.2f, -1.7f, 0, true);
    public static final RobotPose2D Left3 = new RobotPose2D(2.2f, -2.0f, 0, true);
    public static final RobotPose2D Left4 = new RobotPose2D(2.2f, -2.3f, 0, true);
    public static final RobotPose2D Left5 = new RobotPose2D(2.2f, -2.6f, 0, true);
    
    
    public static final RobotPose2D dishesTableCenter1 = new RobotPose2D(2.2f, -0.5f, 0, true);        
    public static final RobotPose2D dishesTableCenter2 = new RobotPose2D(2.2f, 0.5f, 0, true);
    public static final RobotPose2D Center1 = new RobotPose2D(2.2f, 0.6f, 0, true);
    public static final RobotPose2D Center2 = new RobotPose2D(2.2f, 0.3f, 0, true);
    public static final RobotPose2D Center3 = new RobotPose2D(2.2f, 0.0f, 0, true);
    public static final RobotPose2D Center4 = new RobotPose2D(2.2f, -0.3f, 0, true);
    public static final RobotPose2D Center5 = new RobotPose2D(2.2f, -0.6f, 0, true);
    
    public static final RobotPose2D dishesTableRight = new RobotPose2D(2.2f, 2, 0, true);
    public static final RobotPose2D Right1 = new RobotPose2D(2.2f, 2.6f, 0, true);
    public static final RobotPose2D Right2 = new RobotPose2D(2.2f, 2.3f, 0, true);
    public static final RobotPose2D Right3 = new RobotPose2D(2.2f, 2.0f, 0, true);
    public static final RobotPose2D Right4 = new RobotPose2D(2.2f, 1.7f, 0, true);
    public static final RobotPose2D Right5 = new RobotPose2D(2.2f, 1.4f, 0, true);

    
    public static final ArrayList<RobotPose2D> foodPosition = new ArrayList<RobotPose2D>() {{
        add(Left1);
        add(Left2);
        add(Left3);
        add(Left4);
        add(Left5);
        add(Center1);
        add(Center2);
        add(Center3);
        add(Center4);
        add(Center5);
        add(Right1);
        add(Right2);
        add(Right3);
        add(Right4);
        add(Right5);
    }};
    
 
    // Declare safe zone
    public static final ArrayList<RobotPose2D> safeZone = new ArrayList<RobotPose2D>() {{
        add(startPoint1);
        add(startPoint2);
        add(topCenter);
        add(leftPoint);
        add(rightPoint);
        add(bottomCenter);
        add(dishesTableLeft);
        add(dishesTableCenter1);
        add(dishesTableCenter2);
        add(dishesTableRight);
        add(bottomLeft);
        add(bottomRight);
    }};    

    //add target point to list :(
    public static final ArrayList<RobotPose2D> target = new ArrayList<RobotPose2D>(){{
        add(table1);
        add(table2);
        add(table3);
        add(table4);
        add(table5);
        add(dishesTableLeft);
        add(dishesTableCenter1);
        add(dishesTableCenter2);
        add(dishesTableRight);
    }};

    //set radius for safe zone
    public static final float zoneRadius = 0.2f;


    // distance from point a to point b
    public static float distance(RobotPose2D a, RobotPose2D b) {
        float result;
        result = (float)Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
        return result;
    }

    // check if point b inside safe zone a
    public static boolean inside(RobotPose2D a, RobotPose2D b) {
        if( a.x + zoneRadius >= b.x && a.y + zoneRadius >= b.y ) {
            return  true;
        }
        return  false;
    }

    //check if target accessible from a zone "A" to target B
    public static boolean accessible(RobotPose2D a, RobotPose2D b) {
        if( a.equals(startPoint1) ) {
            if( b.equals(table1) || b.equals(table2) || b.equals(table4) || b.equals(table5) 
              ||b.equals(dishesTableLeft)) {
                return true;
            } return false;
        }
        if( a.equals(startPoint2) ) {
        	if( b.equals(table1) || b.equals(table2) || b.equals(table3) || b.equals(table5) 
              ||b.equals(dishesTableRight)) {
                return true;
            } return false;
        }
        if( a.equals(topCenter) ) {
            if( b.equals(table1) || b.equals(table3) || b.equals(table4) || b.equals(leftPoint)
             || b.equals(rightPoint) || b.equals(startPoint1) || b.equals(startPoint2)) {
                return true;
            } return false;
        }
        if( a.equals(leftPoint) ) {
            if( b.equals(table1) || b.equals(table2) || b.equals(table4) || b.equals(dishesTableCenter1) 
             || b.equals(dishesTableCenter2) ) {
                return true;
            } return false;
        }
        if( a.equals(rightPoint) ) {
            if( b.equals(table1) || b.equals(table3) || b.equals(dishesTableCenter1) || b.equals(dishesTableCenter2) ) {
                return true;
            } return false;
        }
        if( a.equals(bottomCenter) ) {
            if( b.equals(table1) || b.equals(table2) || b.equals(table5)
            || b.equals(dishesTableCenter1) || b.equals(dishesTableCenter2) || b.equals(dishesTableLeft) || b.equals(dishesTableRight)) {
                return true;
            } return false;
        }
        if( a.equals(dishesTableLeft) ) {
            if( b.equals(dishesTableCenter2) || b.equals(table5)
            || b.equals(bottomCenter) || b.equals(dishesTableCenter1) || b.equals(dishesTableRight)
            || b.equals(bottomLeft) || b.equals(bottomRight)) {
                return true;
            } return false;
        }
        if( a.equals(dishesTableCenter1) ) {
            if( b.equals(table2) || b.equals(table3)
            || b.equals(table4) || b.equals(table5) || b.equals(rightPoint)              			
            || b.equals(leftPoint) || b.equals(dishesTableLeft) || b.equals(dishesTableRight)) {
                return true;
            } return false;
        }
        if( a.equals(dishesTableCenter2) ) {
            if( b.equals(table1) || b.equals(table2) || b.equals(table3)
            || b.equals(table4) || b.equals(table5) || b.equals(rightPoint)	
            || b.equals(leftPoint) || b.equals(dishesTableLeft) || b.equals(dishesTableRight)) {
                return true;
            } return false;
        }
        if( a.equals(dishesTableRight) ) {
            if( b.equals(table2) || b.equals(table5) || b.equals(dishesTableCenter2)
            || b.equals(bottomCenter) || b.equals(dishesTableCenter1) || b.equals(dishesTableLeft)
            || b.equals(bottomLeft) || b.equals(bottomRight)) {
                return true;
            } return false;            
        }
        if( a.equals(bottomLeft) ) {
        	if( b.equals(table4) || b.equals(dishesTableLeft) || b.equals(table5)) {
        		return true;
        	} return false;        		
        }
        if( a.equals(bottomRight) ) {
        	if( b.equals(table3) || b.equals(dishesTableRight)) {
        		return true;
        	} return false;        		
        }         
        return false;
    }       

    public static Path getPath(RobotPose2D from, RobotPose2D to) {    	
        ArrayList<RobotPose2D> path = new ArrayList<RobotPose2D>();
        int targetId = 1;
        String action = "get";
        Path result = new Path(path, targetId, action);
        
      //Set action
        if(to.equals(table1) || to.equals(table2) || to.equals(table3)
        		|| to.equals(table4) || to.equals(table5)) {
        	result.setAction("put");
        }
        
        //Find path to get food
        RobotPose2D toFood = new RobotPose2D();
        if(to.equals(dishesTableCenter1) || to.equals(dishesTableCenter2)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId+4);
        }
        
        if(to.equals(dishesTableLeft)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId-1);
        }
        
        if(to.equals(dishesTableRight)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId+9);
        }

        // special fixed path
        if (startPoint1.equals(from)) {
            if ( to.equals(dishesTableLeft) || to.equals(dishesTableCenter1) 
            	|| to.equals(dishesTableCenter2) || to.equals(dishesTableRight) ) {
                path.add(bottomLeft);
                path.add(toFood);
                result.setListPoint(path);                
                return(result);
            }
        }

        if (startPoint2.equals(from)) {
        	if ( to.equals(dishesTableLeft) || to.equals(dishesTableCenter1) 
                	|| to.equals(dishesTableCenter2) || to.equals(dishesTableRight) ) {
                    path.add(bottomRight);
                    path.add(toFood);
                    result.setListPoint(path);                    
                    return(result);
                }
        }

        // check if it start from one of the safe zone
        int startZone = -1;
        
        if(inside(dishesTableLeft, from)) {
        	startZone = 6; 
        }
        
        if(inside(dishesTableRight, from)) {
        	startZone = 9;
        }
                
        if(startZone == -1) {
        	for( int i=0; i<safeZone.size(); i++) {        	
                if( from.equals(safeZone.get(i)) ) {
                    startZone = i; 
                    break;
                }
            }
        }        

        // if it a random point on map
        if( startZone == -1 ) {
            double minDistance = 1000000;
            for(int i = 2; i<safeZone.size(); i++) {
                double curDistance = distance(safeZone.get(i), from);
                // assume the nearest is accessible zone
                if( curDistance < minDistance ) {
                    minDistance = curDistance;
                    startZone = i;
                }
            }
            // move to start zone
            path.add( safeZone.get(startZone) );
        }

        // if start from a zone
        if( !accessible(safeZone.get(startZone), to)) {
            // find the nearest safe zone that accessible to target
            float minDistance = 1000000f;
            RobotPose2D nextSafeZone = safeZone.get(startZone);
            for(int i=2; i<safeZone.size(); i++) {
                if( i!= startZone ) {
                    RobotPose2D cur = safeZone.get(i);
                    float curDistance =  distance(safeZone.get(startZone), cur);
                    if( accessible(cur, to) && curDistance < minDistance) {
                        minDistance = curDistance;
                        nextSafeZone = cur;
                    }
                }
            }
            path.add(nextSafeZone);
        }
        
        //if target is table then choose one of three destination
        float minDes;
        RobotPose2D next = new RobotPose2D();
        if(to.equals(table1)) {
        	if(path.size() != 0) {
        		RobotPose2D cur = path.get(path.size() - 1);
        		minDes = distance(cur, table1Left);
        		next = table1Left;
        		if( minDes > distance(cur, table1Right) ) {
        			next = table1Right;
        		}
        	}
        	path.add(next);
        	result.setListPoint(path);        	
        	return result;
        } else
        if(to.equals(table2)) {
            path.add(table2Left);            
            result.setListPoint(path);
            return result;
        } else
        if(to.equals(table3)) {
           	path.add(table3Right);           	
           	result.setListPoint(path);
           	return result;
        } else
        if(to.equals(table4)) {
           	path.add(table4Left);           	
           	result.setListPoint(path);
           	return result;
        } else
        if(to.equals(table5)) {
        	path.add(table5Front);        	
        	result.setListPoint(path);
        	return result;
        }
        	        
        path.add(to);
        result.setListPoint(path);
        return result;        		
    }
    
    public static boolean inside_section(RobotPose2D point, float x1, float x2, float y1, float y2) {
    	if (point.x > x2 || point.x < x1 || point.y < y1 || point.y > y2){
    		return false;
    	}    		
    	return true;
    }
    
    public static Path getPath_ver2(RobotPose2D from, RobotPose2D to) {    	
        ArrayList<RobotPose2D> path = new ArrayList<RobotPose2D>();
        int targetId = 1;
        String action = "get";
        Path result = new Path(path, targetId, action);
        
        //Set action
        if(to.equals(table1) || to.equals(table2) || to.equals(table3)
        		|| to.equals(table4) || to.equals(table5)) {
        	result.setAction("put");
        }
        
        // Find path to get food
        RobotPose2D toFood = new RobotPose2D();
        if(to.equals(dishesTableCenter1) || to.equals(dishesTableCenter2)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId+4);
        }
        
        if(to.equals(dishesTableLeft)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId-1);
        }
        
        if(to.equals(dishesTableRight)) {
        	toFood = foodPosition.get(WaboState.currentOrder.foodId+9);
        }

        // special fixed path
        if (startPoint1.equals(from)) {
            if ( to.equals(dishesTableLeft) || to.equals(dishesTableCenter1) 
            	|| to.equals(dishesTableCenter2) || to.equals(dishesTableRight) ) {
                path.add(bottomLeft);
                path.add(toFood);
                result.setListPoint(path);
                return(result);
            }
        }

        if (startPoint2.equals(from)) {
        	if ( to.equals(dishesTableLeft) || to.equals(dishesTableCenter1) 
                	|| to.equals(dishesTableCenter2) || to.equals(dishesTableRight) ) {
                    path.add(bottomRight);
                    path.add(toFood);
                    result.setListPoint(path);
                    return(result);
                }
        }
        
        // Bring food to table
        if(to.equals(table1) || to.equals(table2) || to.equals(table3)
        		|| to.equals(table4) || to.equals(table5)) {
        	result = PathToTable.pathToTable(from, to);
        	return result;
        }
        
        // Come back to bar 
        if(to.equals(dishesTableLeft) || to.equals(dishesTableCenter1)
        		|| to.equals(dishesTableCenter2) || to.equals(dishesTableRight)) {
        	result = PathToBar.pathToBar(from, to);
        	return result;
        	
        }

        // check if it start from one of the safe zone
        int startZone = -1;
        
        if(inside(dishesTableLeft, from)) {
        	startZone = 6; 
        }
        
        if(inside(dishesTableRight, from)) {
        	startZone = 9;
        }
                
        if(startZone == -1) {
        	for( int i=0; i<safeZone.size(); i++) {        	
                if( from.equals(safeZone.get(i)) ) {
                    startZone = i; 
                    break;
                }
            }
        }        

        // if it a random point on map
        if( startZone == -1 ) {
            double minDistance = 1000000;
            for(int i = 2; i<safeZone.size(); i++) {
                double curDistance = distance(safeZone.get(i), from);
                // assume the nearest is accessible zone
                if( curDistance < minDistance ) {
                    minDistance = curDistance;
                    startZone = i;
                }
            }
            // move to start zone
            path.add( safeZone.get(startZone) );
        }

        // if start from a zone
        if( !accessible(safeZone.get(startZone), to)) {
            // find the nearest safe zone that accessible to target
            float minDistance = 1000000f;
            RobotPose2D nextSafeZone = safeZone.get(startZone);
            for(int i=2; i<safeZone.size(); i++) {
                if( i!= startZone ) {
                    RobotPose2D cur = safeZone.get(i);
                    float curDistance =  distance(safeZone.get(startZone), cur);
                    if( accessible(cur, to) && curDistance < minDistance) {
                        minDistance = curDistance;
                        nextSafeZone = cur;
                    }
                }
            }
            path.add(nextSafeZone);
        }                
        	        
        path.add(to);
        result.setListPoint(path);
        return result;        		
    }
    
    public static void main(String[] args) {
        WaboAlgorithm newWabo = new WaboAlgorithm();
        // start point checking
        RobotPose2D start = new RobotPose2D(-2.5f, -2.5f, 0, true);

        // destination point checking
        RobotPose2D to = new RobotPose2D(2.5f, 2.5f, 0, true);
//
//        //check distance
//        double testVar = newWabo.distance(start, to);
//        System.out.printf("%.1f \n", testVar);

//        ArrayList<RobotPose2D> path = newWabo.getPath(start, to);
//        for (int i=0; i<path.size(); i++) {
//            System.out.printf("%f %f \n", path.get(i).x, path.get(i).y);
//        }
    }
}
