package com.nodalexchange.elevator.operation;

import java.util.Collection;
import java.util.Iterator;

import com.nodalexchange.elevator.ElevatorWaitingAreaInfo;
import com.nodalexchange.elevator.Elevator.DirectionEnum;


class BuildingImpl implements Building {
	private ElevatorWaitingArea[] elevatorWaitingAreas = null;
	private Elevator[] elevators = null;
	
	BuildingImpl(int numberOfFloors, int numberOfElevators, int elevatorCapacity, int elevatorLoadingTime) {
		this.elevatorWaitingAreas = new LobbyImpl[numberOfFloors];
		for (int i=0; i < numberOfFloors; i++) {
			this.elevatorWaitingAreas[i] = new LobbyImpl(String.valueOf(i));
		}

		this.elevators = new Elevator[numberOfElevators];
		for (int i=0; i < numberOfElevators; i++) {
			this.elevators[i] = new ElevatorImpl(i, numberOfFloors, elevatorCapacity, elevatorLoadingTime);
		}
	}
	
	public ElevatorWaitingArea getElevatorWaitingArea(int floor) {
		return this.elevatorWaitingAreas[floor];
	}

	public ElevatorWaitingAreaInfo getElevatorWaitingAreaInfo(int floor) {
		return this.elevatorWaitingAreas[floor];
	}

	public Elevator getElevator(int elevatorNumber) {
		return this.elevators[elevatorNumber];
	}

	public void registerPassengers(Collection<Passenger> nextWave) {
		Iterator<Passenger> iterator = nextWave.iterator();
		while (iterator.hasNext()) {
			Passenger passenger = (Passenger) iterator.next();
			int passengerFloor = passenger.getStartingFloor();
			if (passenger.goingUp()) {
				this.getElevatorWaitingArea(passengerFloor).addPassengerGoingUp(passenger);
				if (!this.hasAvailableSpotInOpenElevatorGoingUp(passengerFloor)) {
					this.getElevatorWaitingArea(passengerFloor).pushUpButton();
				}
			}
			else {
				this.getElevatorWaitingArea(passengerFloor).addPassengerGoingDown(passenger);
				if (!this.hasAvailableSpotInOpenElevatorGoingDown(passengerFloor)) {
					this.getElevatorWaitingArea(passengerFloor).pushDownButton();
				}
			}
		}
	}
	

	private boolean hasAvailableSpotInOpenElevatorGoingUp(int passengerFloor) {
		boolean returnValue = false;
		Elevator nextElevator = null;
		for (int i=0; i < this.getNumberOfElevators(); i++) {
			nextElevator = this.getElevator(i);
			if (nextElevator.getCurrentFloor() == passengerFloor && nextElevator.isLoading() && nextElevator.getButtonToAnswerAtNextStop() == DirectionEnum.UP && nextElevator.getPassengerCount() < nextElevator.getCapacity()) {
				returnValue = true;
				break;
			}
		}
		
		return returnValue;
	}

	private boolean hasAvailableSpotInOpenElevatorGoingDown(int passengerFloor) {
		boolean returnValue = false;
		Elevator nextElevator = null;
		for (int i=0; i < this.getNumberOfElevators(); i++) {
			nextElevator = this.getElevator(i);
			if (nextElevator.getCurrentFloor() == passengerFloor && nextElevator.isLoading() && nextElevator.getButtonToAnswerAtNextStop() == DirectionEnum.DOWN && nextElevator.getPassengerCount() < nextElevator.getCapacity()) {
				returnValue = true;
				break;
			}
		}
		
		return returnValue;
	}

	public int getNumberOfFloors() {
		return this.elevatorWaitingAreas.length;
	}
	
	public int getNumberOfElevators() {
		return this.elevators.length;
	}
}
