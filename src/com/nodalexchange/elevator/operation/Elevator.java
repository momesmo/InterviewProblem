package com.nodalexchange.elevator.operation;

import java.util.Set;

interface Elevator extends com.nodalexchange.elevator.Elevator {
	public Set<Passenger> addPassengers(Set<Passenger> passengers);
	public boolean isLoading();
	public boolean isArriving(Building building);
	public int getPassengerCount();
	public void move();
	public void unload();
	public void openDoors();
	public void closeDoors();
	public void load(Building building);
	public void keepDoorsOpen();
}