package com.nodalexchange.elevator.operation;

import java.util.Collection;

interface Building extends com.nodalexchange.elevator.Building {
	public void registerPassengers(Collection<Passenger> nextWave);
	public ElevatorWaitingArea getElevatorWaitingArea(int floor);
	public Elevator getElevator(int number);
}
