package hu.bme.mit.train.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.system.TrainSystem;
import hu.bme.mit.train.sensor.TrainSensorImpl;
import hu.bme.mit.train.controller.TrainControllerImpl;
import hu.bme.mit.train.user.TrainUserImpl;

public class TrainSystemTest {

	TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	TrainUser mUser;
	TrainController mController;
	TrainSensor mSensor;

	@Before
	public void before() {
		TrainSystem system = new TrainSystem();
		controller = system.getController();
		sensor = system.getSensor();
		user = system.getUser();

		sensor.overrideSpeedLimit(50);

		mUser = mock(TrainUserImpl.class);
		mController = mock(TrainControllerImpl.class);
		mSensor = new TrainSensorImpl(mController, mUser);
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

	@Test
	public void AlarmLessThanZeroTest(){
		mSensor.overrideSpeedLimit(-10);
		verify(mUser,times(1)).setAlarmState(true);
	}

	@Test
	public void AlarmGreaterThan500Test(){
		mSensor.overrideSpeedLimit(501);
		verify(mUser,times(1)).setAlarmState(true);
	}


	@Test
	public void AlarmLessThanFiftyPercentTest(){
		when(mController.getReferenceSpeed()).thenReturn(10);
		mSensor.overrideSpeedLimit(1);
		verify(mUser,times(1)).setAlarmState(true);
	
	}

	@Test
	public void AlarmMoreThanFiftyPercentTest(){
		when(mController.getReferenceSpeed()).thenReturn(10);
		mSensor.overrideSpeedLimit(6);
		verify(mUser,times(0)).setAlarmState(true);
	}





	
}
