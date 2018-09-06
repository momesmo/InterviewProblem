package com.nodalexchange.elevator;

public interface Elevator {
	public enum DirectionEnum {UP,DOWN}

	public int getCapacity();
	public int getCurrentFloor();
	public int getNextStop();
	public DirectionEnum getButtonToAnswerAtNextStop();
	public int getElevatorNumber();
	public boolean[] getButtonStates();
	public void setNewInstruction(int nextStop, DirectionEnum nextLobbyButtonToAnswer);
}
