package com.nodalexchange.elevator.operation;

import java.util.List;
import java.util.Vector;

class SimplePassengerGenerator implements PassengerGenerator {
	public List<Passenger> getNextCustomerBatch(int currentCycle, int numberOfFloors) {
		List<Passenger> returnValue = new Vector<Passenger>();
		int i = currentCycle % (numberOfFloors - 1) + 1;

		returnValue.add(PassengerFactory.createPassenger(0, i));
		returnValue.add(PassengerFactory.createPassenger(i, 0));

		return returnValue;
	}
}
