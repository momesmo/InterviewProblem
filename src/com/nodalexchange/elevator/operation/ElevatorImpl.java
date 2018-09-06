package com.nodalexchange.elevator.operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nodalexchange.elevator.operation.Building;

class ElevatorImpl implements Elevator {
	private int loadCyclesRemaining = -1; //to be initialized in constructor
	private int elevatorNumber = -1; //to be initialized in constructor
	private int capacity = -1; //to be initialized in constructor
	private int loadingTime = -1; //to be initialized in constructor
	private int currentFloor = -1; //to be initialized in constructor
	private int nextStop = -1; //to be initialized in constructor
	
	private boolean[] buttonsPushed = null;
	private List<Passenger> currentPassengers = new ArrayList<Passenger>();
	private DirectionEnum buttonToAnswerAtNextStop = DirectionEnum.UP;
	private boolean justMoved;

	ElevatorImpl(int elevatorNumber, int numberOfFloors, int capacity, int loadingTime) {
		buttonsPushed = new boolean[numberOfFloors];
		this.elevatorNumber = elevatorNumber;
		this.capacity = capacity;
		this.loadingTime = loadingTime;
		this.loadCyclesRemaining = loadingTime;
		this.currentFloor = 0; //all elevators start at bottom floor
		this.nextStop = 0; //all elevators start with no plan to go anywhere
	}

	public int getCurrentFloor() { return this.currentFloor; }
	public int getElevatorNumber() { return this.elevatorNumber; }
	public boolean isLoading() { return this.loadCyclesRemaining > 0; }
	public boolean[] getButtonStates() { return this.buttonsPushed; }
	public void setNewInstruction(int nextStop, DirectionEnum buttonToAnswerAtNextStop) {
		this.nextStop = nextStop;
		this.buttonToAnswerAtNextStop = buttonToAnswerAtNextStop; 
	}
	public int getNextStop() { return this.nextStop; }
	public DirectionEnum getButtonToAnswerAtNextStop() { return this.buttonToAnswerAtNextStop; }

	public Set<Passenger> addPassengers(Set<Passenger> passengers) {
		Set<Passenger> passengersLeavingLobby = new HashSet<Passenger>();
		Util.logEvent(passengers.size() + " passengers trying to get on elevator " + this.getElevatorNumber() + " on floor " + this.getCurrentFloor());
		for (Passenger passenger : passengers) {
			if (this.getPassengerCount() != this.capacity) {
				this.addPassenger(passenger);
				passengersLeavingLobby.add(passenger);
			}
			else {
				break; //don't try to load any more passengers
			}
		}
		
		return passengersLeavingLobby;
	}

	private void addPassenger(Passenger passenger) {
		passenger.setCycleGettingOn(SystemLauncher.getCurrentCycle());
		passenger.setElevatorNumber(this.elevatorNumber);
		this.buttonsPushed[passenger.getDestinationFloor()] = true;
		this.currentPassengers.add(passenger);
	}

	public void move() {
		if (this.loadCyclesRemaining == 0) {
			if (this.getNextStop() > this.currentFloor) {//going up
				this.currentFloor++;
				this.justMoved = true;
			}
			else if (this.getNextStop() < this.currentFloor){//going down
				this.currentFloor--;
				this.justMoved = true;
			}
		}
		else {
			this.justMoved = false;
		}
	}
	
	public void unload() {
		//clear inside button for this floor
		this.buttonsPushed[this.currentFloor] = false;

		//get exiting passengers 
		Set<Passenger> exitingPassengers = new HashSet<Passenger>();
		for (Passenger nextPassenger : this.currentPassengers) {
			if (nextPassenger.getDestinationFloor() == this.currentFloor) {
				exitingPassengers.add(nextPassenger);
				nextPassenger.setCycleGettingOff(SystemLauncher.getCurrentCycle());
			}
		}
		
		Util.logEvent(exitingPassengers.size() + " passengers got off elevator " + this.getElevatorNumber() + " on floor " + this.getCurrentFloor());

		this.currentPassengers.removeAll(exitingPassengers);
		Util.endTrip(exitingPassengers);
	}
	
	public void openDoors() {
		this.loadCyclesRemaining = this.loadingTime;
	}

	public void closeDoors() {
		this.loadCyclesRemaining = 0;
	}

	public int getPassengerCount() {
		return this.currentPassengers.size();
	}

	public void load(Building building) {
		int floor = this.getCurrentFloor();
		ElevatorWaitingArea currentLobby = building.getElevatorWaitingArea(floor);
		Set<Passenger> passengersLeavingLobby = null;
		if (this.getButtonToAnswerAtNextStop() == DirectionEnum.UP) {
			passengersLeavingLobby = this.addPassengers(currentLobby.getPassengersGoingUp());
			currentLobby.removePassengersGoingUp(passengersLeavingLobby);
		}
		else {
			passengersLeavingLobby = this.addPassengers(currentLobby.getPassengersGoingDown());
			currentLobby.removePassengersGoingDown(passengersLeavingLobby);
		}
	}
	
	public boolean isArriving(Building building) {
		return this.justMoved && (this.currentFloor == this.getNextStop());
	}

	public void keepDoorsOpen() {
		this.loadCyclesRemaining--;
	}

	public int getCapacity() {
		return this.capacity;
	}

}
