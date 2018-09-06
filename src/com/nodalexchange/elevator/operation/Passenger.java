package com.nodalexchange.elevator.operation;

interface Passenger {
	public void startTrip(int cycle);
	public void stopTrip(int cycle);
	public void setCycleGettingOn(int cycleGettingOn);
	public void setCycleGettingOff(int cycleGettingOff);
	public void setElevatorNumber(int elevatorNumber);
	public boolean goingUp();
	public boolean goingDown();
	public int getTripTime();
	public int getStartingFloor();
	public int getDestinationFloor();
	public int getCycleGettingOn();
	public int getCycleGettingOff();
	public int getElevatorNumber();

}