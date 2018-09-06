package com.nodalexchange.elevator.operation;

import java.util.List;

interface PassengerGenerator {
	public List<Passenger> getNextCustomerBatch(int currentCycle, int numberOfFloors);
}
