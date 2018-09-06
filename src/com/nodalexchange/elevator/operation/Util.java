package com.nodalexchange.elevator.operation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nodalexchange.elevator.operation.Building;


class Util {
	static void startTrip(List<Passenger> c) {
		Iterator<Passenger> iterator = c.iterator();
		while (iterator.hasNext()) {
			Passenger passenger = (Passenger) iterator.next();
			passenger.startTrip(SystemLauncher.currentCycle);
		}
	}

	static void endTrip(Set<Passenger> c) {
		Iterator<Passenger> iterator = c.iterator();
		while (iterator.hasNext()) {
			Passenger passenger = (Passenger) iterator.next();
			passenger.stopTrip(SystemLauncher.getCurrentCycle());
		}
	}

	static void reportPassengerExperience(List<Passenger> allPassengers) {
		Map<String, Integer> resultsMap = new HashMap<String, Integer>();
		
		double averageTripTime = 0;
		int totalTripTime = 0;
	
		String key = null;
		for (Passenger passenger : allPassengers) {
			totalTripTime += passenger.getTripTime();
			key = format(passenger.getTripTime(), false);
			Integer currentValue = resultsMap.get(key);
			if (currentValue == null) {
				currentValue = new Integer(0);
			}
			resultsMap.put(key, new Integer(currentValue.intValue() + 1));
		}

//		List<String> keys = new ArrayList<String>(resultsMap.keySet());
//		Collections.sort(keys);
//		for (String nextKey : keys) {
//			System.out.println(nextKey + " cycles: " + resultsMap.get(nextKey));
//		}

		averageTripTime = (double)totalTripTime / (double)allPassengers.size();
		
		System.out.println("");
		System.out.println("");
		System.out.println("************** RESULTS ******************");
		System.out.println("All passengers have arrived.");
		System.out.println("Average cycles per customer trip: " + averageTripTime);
		System.out.println("*****************************************");
	}

	private static String format(int i, boolean loading) {
		String wrapper = " ";
		if (loading) {
			wrapper = "-";
		}
		if (i < 10) {
			return wrapper + "00" + i + wrapper;
		}
		else if (i < 100) {
			return wrapper + "0" + i + wrapper;
		}
		else
			return wrapper + i + wrapper;
	}
	
	static void logEvent(String s) {
		//System.out.println("At t = " + SystemLauncher.getCurrentCycle() + ": " + s);
	}
	
	static void printBuilding(Building building) {
		System.out.println();
		System.out.println();
		System.out.println();
		
		System.out.println("Status after " + SystemLauncher.getCurrentCycle() + " cycles:");
		System.out.println("");
		
		for (int i=building.getNumberOfFloors()-1; i>=0; i--) {
			System.out.print("[FLOOR " + i + "]  ");
			for (int j=0; j<building.getNumberOfElevators(); j++) {
				Elevator nextElevator = building.getElevator(j);
				if (nextElevator.getCurrentFloor() ==  i) {
					System.out.print(Util.format(nextElevator.getPassengerCount(), nextElevator.isLoading()));
					System.out.print("  ");
				}
				else {
					System.out.print("XXXXX  ");
				}
			}
			System.out.print("  ");
			System.out.print(Util.format(building.getElevatorWaitingArea(i).getPassengersGoingUp().size(), false));
			System.out.print("       ");
			System.out.println(Util.format(building.getElevatorWaitingArea(i).getPassengersGoingDown().size(), false));
		}

		StringBuffer variableDashes = new StringBuffer("");
		StringBuffer elevatorLabels = new StringBuffer("");
		StringBuffer variableSpaces = new StringBuffer("");
		for (int j=0; j<building.getNumberOfElevators(); j++) {
			variableDashes.append("-------");
			elevatorLabels.append("ELEV" + j + "  ");
			variableSpaces.append("       ");
		}
		System.out.print("           ");
		System.out.print(variableDashes.toString());
		System.out.println("--------------------");

		System.out.print("           ");
		System.out.print(elevatorLabels.toString());
		System.out.println(" WAITING     WAITING");
		
		System.out.print("           ");
		System.out.print(variableSpaces.toString());
		System.out.println(" TO GO UP   TO GO DOWN");

		System.out.println("");
		System.out.println("Elevator inside buttons currently selected:");
		for (int j=0; j<building.getNumberOfElevators(); j++) {
			System.out.println(getButtonDisplay(building.getElevator(j)));
		}
	}
	
	private static String getButtonDisplay(Elevator e) {
		String returnValue = "ELEVATOR " + e.getElevatorNumber() + ": ";
		boolean[] states = e.getButtonStates();
		for (int i=0; i<states.length; i++) {
			if (states[i]) {
				returnValue += i + " ";
			}
		}
		
		return returnValue;
	}
}
