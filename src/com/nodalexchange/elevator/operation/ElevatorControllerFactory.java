package com.nodalexchange.elevator.operation;

import com.nodalexchange.elevator.ElevatorController;

class ElevatorControllerFactory {
	static ElevatorController buildController(String controllerClassName) {
		ElevatorController returnValue = null;
		try {
			returnValue = (ElevatorController)Class.forName(controllerClassName).newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException("Error building an elevator controller", e);
		}
		
		return returnValue;
	}
}
