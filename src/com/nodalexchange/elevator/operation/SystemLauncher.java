package com.nodalexchange.elevator.operation;

import com.nodalexchange.elevator.ElevatorController;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


class SystemLauncher {
	static int currentCycle = 0;
	static Building building = null;
	static List<String> eventLog = new ArrayList<String>();
	
	//arg 0: floors in building
	//arg 1: number of elevators
	//arg 2: elevator capacity
	//arg 3: cycles required for loading
	//arg 4: classname of passenger generator
	//arg 5: classname of elevator controller implementation
	//arg 6: number of cycles to run and measure
	public static void main(String[] args) {
		SystemLauncher.building = BuildingFactory.buildBuilding(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
		PassengerGenerator passengerGenerator = PassengerGeneratorFactory.buildPassengerGenerator(args[4]);
		ElevatorController controller = ElevatorControllerFactory.buildController(args[5]);
		int totalPassengerGenerationCycles = Integer.parseInt(args[6]);
		
		List<Passenger> allPassengers = new Vector<Passenger>();

		while (!SystemLauncher.passengersDoneRiding(allPassengers)) {
			if (SystemLauncher.currentCycle < totalPassengerGenerationCycles) {
				SystemLauncher.generateNewPassengers(allPassengers, passengerGenerator);
			}
			SystemLauncher.currentCycle++;
			Elevator nextElevator = null;
			for (int i=0; i<SystemLauncher.building.getNumberOfElevators(); i++) {
				nextElevator = SystemLauncher.building.getElevator(i);
				nextElevator.move();
				
				if (nextElevator.isLoading()) {
					nextElevator.keepDoorsOpen();
					nextElevator.load(SystemLauncher.building);
				}
				else if (nextElevator.isArriving(SystemLauncher.building)) {//lobby passengers to pick up or elevator passengers to drop off)
					nextElevator.openDoors();
					nextElevator.unload();
					nextElevator.load(SystemLauncher.building);
				}
				else { //loading cycle finished
					nextElevator.closeDoors();
				}
			}
			
			controller.cycleElapsed(building);
			Util.printBuilding(SystemLauncher.building);
		}
		
		Util.reportPassengerExperience(allPassengers);
	}
	
	public static int getCurrentCycle() { return SystemLauncher.currentCycle; }
	
	private static boolean passengersDoneRiding(List<Passenger> allPassengers) {
		boolean returnValue = true;
		if (allPassengers.size() == 0) {
			returnValue = false; //haven't loaded passengers yet
		}
		else {
			for (Passenger passenger : allPassengers) {
				if (passenger.getCycleGettingOff() == 0) {
					returnValue = false;
					break;
				}
			}
		}
		return returnValue;
	}

	private static void generateNewPassengers(List<Passenger> allPassengers, PassengerGenerator passengerGenerator) {
		//generate new passengers
		List<Passenger> nextWave = passengerGenerator.getNextCustomerBatch(SystemLauncher.currentCycle, SystemLauncher.building.getNumberOfFloors());
		allPassengers.addAll(nextWave);
		
		//start trip timers
		Util.startTrip(nextWave);
				
		//register customers with lobby
		SystemLauncher.building.registerPassengers(nextWave);
	}

}
