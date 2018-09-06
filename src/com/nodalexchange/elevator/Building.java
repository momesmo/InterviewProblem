package com.nodalexchange.elevator;

import com.nodalexchange.elevator.Elevator;
import com.nodalexchange.elevator.ElevatorWaitingAreaInfo;

public interface Building {
	public int getNumberOfFloors();
	public int getNumberOfElevators();
	public ElevatorWaitingAreaInfo getElevatorWaitingAreaInfo(int floor);
	public Elevator getElevator(int elevatorNumber);
}