package experimental;
import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.util.*;

/**
 * software abstraction of a light sensor rotating in a horizontal plane, driven
 * by a motor Records the light value and bearing of the brightest light seen
 * during a scan. Compare with LightScanner in Lejos Robotics
 * 
 * @author Roger Glassey
 * 
 */
public class Scanner0 {
	/**
	 * Instance variables
	 */
	NXTRegulatedMotor head;
	LightSensor eye;

	/**
	 * measured angle to the brightest light.
	 */
	private int _angle;
	private int _maxLight;

	/**
	 * 
	 * @param headMotor
	 * @param lightSensor
	 */
	public Scanner0(NXTRegulatedMotor headMotor, LightSensor lightSensor) {
		head = headMotor;
		eye = lightSensor;
		eye.setFloodlight(false);
	}

	/**
	 * 
	 * @param limit
	 */
	public void scanTo(int limit) {
		int light;
		_maxLight = 0;
		head.rotateTo(limit, true);
		while (head.isMoving()) {
			light = eye.getLightValue();
			if (light > _maxLight) {
				_maxLight = light;
				_angle = head.getTachoCount();
			}
		}
	}
}
