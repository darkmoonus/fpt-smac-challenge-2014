package uet.invincible.wabo.mission;


import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.types.RobotMoveTargetPosition;
import com.fpt.robot.vision.RobotObjectDetection.ObjectPose;
import com.fpt.robot.wabo.WaboArm;
import com.fpt.robot.wabo.WaboSMAC;

public class WaboUtils {
	/**
	 * move robot to position (x , y, theta). if robot is not waked up yet, call wakeUp method
	 * @param x
	 * @param y
	 * @param theta
	 * @param wakeUp
	 * @param robot
	 */
	
	
	/**
	 * move robot to position (x , y, theta). if robot is not waked up yet, call wakeUp method
	 * @param target
	 * @param wakeUp
	 * @param robot
	 */
//	public static void move(RobotMoveTargetPosition target,
//			final boolean wakeUp, final Robot robot) {
//
//		move(target.x, target.y, target.theta, wakeUp, robot);
//	}

	public static boolean moveCloserObject(ObjectPose pose,int id,Robot robot) {
		float x = pose.x;
		float y = pose.y;
		@SuppressWarnings("unused")
		float theta = pose.theta * -1;
		double distance = Math.sqrt(x * x + y * y);
		try {
			boolean result = false;
			if (distance > 0.7f) {
				System.out.println("move to x-0.7");
				result = RobotMotionLocomotionController.moveTo(robot, new RobotMoveTargetPosition(x - 0.7f, 0, 0.0f));
				System.out.println("move to x-0.7 "+result);
			}
			//move to near object
			System.out.println("move closer to object, id: " + id);
			result = WaboSMAC.moveCloserToObject(robot, id);
			System.out.println("moveCloserToObject " + result);
			//move more 10cm
			//result = RobotMotionLocomotionController.moveTo(robot,
			//		new RobotMoveTargetPosition(0.07f, 0, 0.0f));
			
			//result = RobotMotionLocomotionController.moveStop(robot);
			System.out.println("move more  " + result);
			return result;
		} catch (RobotException e) {
			e.printStackTrace();
		}
		return false;
	}
}
