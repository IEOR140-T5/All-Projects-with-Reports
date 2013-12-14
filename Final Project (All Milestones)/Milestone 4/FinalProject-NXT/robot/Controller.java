package robot;

import java.io.IOException;
import java.util.ArrayList;

import robot.Message;
import robot.MessageType;
import lejos.geom.Point;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;

/**
 * Control the robot by using queue in order to send message to communicate between PC-NXT
 *  
 */
public class Controller implements CommListener {
	private Navigator navigator;
	private Communicator comm;
	private ArrayList<Message> inbox;
	private Locator locator;

	public Controller(Navigator n, Locator l) {
		System.out.println("Connecting...");
		comm = new Communicator();
		comm.setController(this);
		navigator = n;
		locator = l;
		inbox = new ArrayList<Message>();
		//navigator.addWaypoint(0, 0);
	}

	/**
	 * send the message to nxt, and put it in queue
	 * 
	 * @param m : message to be send/execute
	 */
	public void updateMessage(Message m) {
		System.out.println("Updating messages...");
		if(m.getType() == MessageType.STOP) {
			inbox.clear();
			navigator.stop();
			navigator.clearPath();
		} else {
			inbox.add(m);
		}
	}

	/**
	 * Main method, check the queue and execute
	 */
	public void go() {
		Message currentMessage = new Message(MessageType.STOP, new float[1]);
		while(true) {
			//System.out.println(currentMessage.getType());
			while(!inbox.isEmpty()) {
				System.out.println("Running message!");
				currentMessage = inbox.remove(0);
				execute(currentMessage);
			}
		}
	}
	
	/**
	 * Gets the current pose from the navigator and passes a message
	 * to the communicator for it to send to the PC
	 */
	private void sendPose() {
		Pose pose = navigator.getPoseProvider().getPose();
		float[] array = new float[3];
		array[0] = pose.getX();
		array[1] = pose.getY();
		array[2] = pose.getHeading();
		try {
			comm.send(new Message(MessageType.POS_UPDATE, array));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Crash message to PC
	 */
	private void sendCrashMsg() {
		try {
			comm.send(new Message(MessageType.CRASH, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * take the message and execute it
	 * 
	 * 
	 * @param m - the message
	 */
	private void execute(Message m) {
		Sound.playNote(Sound.PIANO, 450, 15);
		switch(m.getType()) {
		case STOP:
			navigator.stop();
			sendPose();
			break;
		case MOVE:
			float[] data = m.getData();
			navigator.goTo(data[0], data[1]);
			sendPose();
			break;
		case FIX_POS:
			System.out.println("fix pos start");
			locator.setPose(navigator.getPoseProvider().getPose());
			System.out.println("fix pos got the pose from nav");
			locator.locate();
			System.out.println("locator located");
			navigator.getPoseProvider().setPose(locator._pose);
			sendPose();
			System.out.println("fix pos end");
			break;
		case ROTATE:
			((DifferentialPilot) navigator.getMoveController()).rotate(m.getData()[0]);
			sendPose();
			break;
		case TRAVEL:
			((DifferentialPilot) navigator.getMoveController()).travel(m.getData()[0], true);
			sendPose();
			break;
		case SET_POSE:
			locator._pose.setLocation(m.getData()[0], m.getData()[1]);
			locator._pose.setHeading(m.getData()[2]);
			navigator.getPoseProvider().setPose(locator._pose);
			sendPose();
			break;
		default:
			break;
		}
		Sound.playNote(Sound.PIANO, 500, 15);
	} 

}

