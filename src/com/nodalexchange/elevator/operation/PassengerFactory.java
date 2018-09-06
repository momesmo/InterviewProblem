package com.nodalexchange.elevator.operation;

class PassengerFactory {
	static Passenger createPassenger(int startingFloor, int destinationFloor) {
		return new PassengerImpl(startingFloor, destinationFloor);
	}
}
