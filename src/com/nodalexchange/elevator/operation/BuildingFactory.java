package com.nodalexchange.elevator.operation;


class BuildingFactory {
	static Building buildBuilding(int floors, int elevators, int elevatorCapacity, int elevatorLoadingTime) {
		return new BuildingImpl(floors, elevators, elevatorCapacity, elevatorLoadingTime);
	}
}
