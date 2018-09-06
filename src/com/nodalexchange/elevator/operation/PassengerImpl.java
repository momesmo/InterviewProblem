package com.nodalexchange.elevator.operation;

class PassengerImpl implements Passenger {
	private int startingFloor = 0;
	private int destinationFloor = 0;
	private int startingCycle = 0;
	private int totalCyclesInTrip = 0;
	private int cycleGettingOn = 0;
	private int cycleGettingOff = 0;
	private int elevatorNumber = -1;
	
	PassengerImpl(int startingFloor, int destinationFloor) {
		this.startingFloor = startingFloor;
		this.destinationFloor = destinationFloor;
	}
	
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#goingUp()
	 */
	@Override
	public boolean goingUp() { return this.destinationFloor > this.startingFloor; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#goingDown()
	 */
	@Override
	public boolean goingDown() { return !this.goingUp(); }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.Passenger#startTrip(int)
	 */
	@Override
	public void startTrip(int cycle) { this.startingCycle = cycle; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.Passenger#stopTrip(int)
	 */
	@Override
	public void stopTrip(int cycle) { this.totalCyclesInTrip = cycle - this.startingCycle; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getTripTime()
	 */
	@Override
	public int getTripTime() { return this.totalCyclesInTrip; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getStartingFloor()
	 */
	@Override
	public int getStartingFloor() { return this.startingFloor; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getDestinationFloor()
	 */
	@Override
	public int getDestinationFloor() { return this.destinationFloor; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.Passenger#setCycleGettingOn(int)
	 */
	@Override
	public void setCycleGettingOn(int cycleGettingOn) { this.cycleGettingOn = cycleGettingOn; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getCycleGettingOn()
	 */
	@Override
	public int getCycleGettingOn() { return cycleGettingOn; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.Passenger#setCycleGettingOff(int)
	 */
	@Override
	public void setCycleGettingOff(int cycleGettingOff) { this.cycleGettingOff = cycleGettingOff; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getCycleGettingOff()
	 */
	@Override
	public int getCycleGettingOff() { return cycleGettingOff; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.Passenger#setElevatorNumber(int)
	 */
	@Override
	public void setElevatorNumber(int elevatorNumber) { this.elevatorNumber = elevatorNumber; }
	/* (non-Javadoc)
	 * @see nodalexchange.elevator.ops.impl.PassengerStatus#getElevatorNumber()
	 */
	@Override
	public int getElevatorNumber() { return elevatorNumber; }
}
