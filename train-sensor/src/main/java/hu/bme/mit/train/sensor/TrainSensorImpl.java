package hu.bme.mit.train.sensor;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.time.*;

public class TrainSensorImpl implements TrainSensor {

	private TrainController controller;
	private TrainUser user;
	private int speedLimit = 5;
	private Table<LocalDateTime, Integer, Integer> tachograph = HashBasedTable.create();

	public TrainSensorImpl(TrainController controller, TrainUser user) {
		this.controller = controller;
		this.user = user;
	}

	@Override
	public int getSpeedLimit() {
		return speedLimit;
	}

	@Override
	public void overrideSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		controller.setSpeedLimit(speedLimit);
		
		//Check for alarm
		int refSpeed = controller.getReferenceSpeed();
		if(speedLimit < 0 || speedLimit > 500 || (refSpeed>0 && (double) speedLimit / (double) refSpeed < 0.5)){
			user.setAlarmState(true);
		}
	}

	@Override
	public void addLog(){
		this.tachograph.put(LocalDateTime.now(), user.getJoystickPosition(), controller.getReferenceSpeed());
	}

	@Override
	public int getLogSize(){
		return this.tachograph.size();
	}
}
