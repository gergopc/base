package hu.bme.mit.train.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.system.TrainSystem;

public class TrainSystemTest {

	TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	
	@Before
	public void before() {
		TrainSystem system = new TrainSystem();
		controller = system.getController();
		sensor = system.getSensor();
		user = system.getUser();

		sensor.overrideSpeedLimit(50);
	}
	
	@Test
	public void OverridingJoystickPosition_IncreasesReferenceSpeed() {
		sensor.overrideSpeedLimit(10);

		Assert.assertEquals(0, controller.getReferenceSpeed());
		
		user.overrideJoystickPosition(5);

		controller.followSpeed();
		Assert.assertEquals(5, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
	}

	@Test
	public void OverridingJoystickPositionToNegative_SetsReferenceSpeedToZero() {
		user.overrideJoystickPosition(4);
		controller.followSpeed();
		user.overrideJoystickPosition(-5);
		controller.followSpeed();
		Assert.assertEquals(0, controller.getReferenceSpeed());
	}

	@Test
	public void MaintainSpeed() {
		user.overrideJoystickPosition(4);
		controller.followSpeed();
		Assert.assertEquals(4, controller.getReferenceSpeed());
		user.overrideJoystickPosition(0);
		controller.followSpeed();
		Assert.assertEquals(4, controller.getReferenceSpeed());
	}

	@Test
	public void EmergencyBreakPressed(){
		user.overrideJoystickPosition(5);
		controller.followSpeed();
		Assert.assertEquals(5, controller.getReferenceSpeed());

		controller.emergencyBreak();
		Assert.assertEquals(0, controller.getReferenceSpeed());
		
	}

	@Test
	public void TachogrpahTest(){
		Assert.assertEquals(sensor.getLogSize(), 0);
		sensor.addLog();
		Assert.assertEquals(sensor.getLogSize(), 1);
	}

	
}
