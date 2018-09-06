package com.nodalexchange.elevator.operation;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class RandomPassengerGenerator implements PassengerGenerator {
	@Override
	public List<Passenger> getNextCustomerBatch(int currentCycle, int numberOfFloors) {
		List<Passenger> returnValue = new Vector<Passenger>();
		Random randGen = new Random(currentCycle);
		int targetFloor = randGen.nextInt(numberOfFloors);
		int originatingFloor = randGen.nextInt(numberOfFloors);
		if (targetFloor != originatingFloor) {
			returnValue.add(PassengerFactory.createPassenger(targetFloor, originatingFloor));
			returnValue.add(PassengerFactory.createPassenger(originatingFloor,targetFloor));		
		}
		return returnValue;
	}

}
