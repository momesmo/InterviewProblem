package com.nodalexchange.elevator.operation;

import java.util.Set;

import com.nodalexchange.elevator.ElevatorWaitingAreaInfo;
import com.nodalexchange.elevator.operation.Passenger;


interface ElevatorWaitingArea extends ElevatorWaitingAreaInfo {
	public Set<Passenger> getPassengersGoingUp();
	public Set<Passenger> getPassengersGoingDown();
	public void addPassengerGoingUp(Passenger passenger);
	public void addPassengerGoingDown(Passenger passenger);
	public void pushUpButton();
	public void pushDownButton();
	public void removePassengersGoingDown(Set<Passenger> passengersLeavingLobby);
	public void removePassengersGoingUp(Set<Passenger> passengersLeavingLobby);
}