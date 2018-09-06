package com.nodalexchange.elevator.operation;

import java.util.HashSet;
import java.util.Set;

import com.nodalexchange.elevator.ElevatorWaitingAreaInfo;


class LobbyImpl implements ElevatorWaitingArea, ElevatorWaitingAreaInfo {
	private String floor = null;
	private boolean upButtonPushed = false;
	private boolean downButtonPushed = false;
	private Set<Passenger> passengersGoingUp = new HashSet<Passenger>();
	private Set<Passenger> passengersGoingDown = new HashSet<Passenger>();
	
	LobbyImpl(String floor) {
		this.floor = floor;
	}

	public boolean isUpButtonPushed() { return this.upButtonPushed; }
	public boolean isDownButtonPushed() { return this.downButtonPushed; }

	public void pushUpButton(){ this.upButtonPushed = true; }
	public void pushDownButton() { this.downButtonPushed = true; }
	String getFloor() { return this.floor; }
	void elevatorGoingDownOpens() { this.downButtonPushed = false; }
	void elevatorGoingUpOpens() { this.upButtonPushed = false; }
	
	public Set<Passenger> getPassengersGoingUp() { return this.passengersGoingUp; }
	public Set<Passenger> getPassengersGoingDown() { return this.passengersGoingDown;}
	
	public void removePassengersGoingUp(Set<Passenger> passengers) {
		this.passengersGoingUp.removeAll(passengers);
		if (this.passengersGoingUp.size() == 0) {
			this.upButtonPushed = false;
		}
	}

	public void removePassengersGoingDown(Set<Passenger> passengers) {
		this.passengersGoingDown.removeAll(passengers);
		if (this.passengersGoingDown.size() == 0) {
			this.downButtonPushed = false;
		}
	}
	
	public void addPassengerGoingUp(Passenger passengerImpl) {
		this.passengersGoingUp.add(passengerImpl);
	}
	
	public void addPassengerGoingDown(Passenger passengerImpl) {
		this.passengersGoingDown.add(passengerImpl);
	}
}
