package com.nodalexchange.elevator.controllers;

import com.nodalexchange.elevator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MyElevatorController implements ElevatorController {
    private enum CycleCases {INITIALIZE, COMPUTE}
    private CycleCases cycleSwitch = CycleCases.INITIALIZE;
    private int numFloors = -1;
    private int numElevators = -1;
    private int topFloor = -1;
    private int bottomFloor = -1;
    private boolean[] waitingAreaUpPushed;
    private boolean[] waitingAreaDownPushed;
    private Elevator[] elevatorArray;
    private boolean[][] elevatorButtonsPushed;

    private void getWaitingAreaAndElevatorButtons(Building building){
        for(int i = 0; i < numFloors; i++){
            waitingAreaUpPushed[i] =  building.getElevatorWaitingAreaInfo(i).isUpButtonPushed();
            waitingAreaDownPushed[i] = building.getElevatorWaitingAreaInfo(i).isDownButtonPushed();
        }
        for(int i = 0; i < numElevators; i++){
            elevatorArray[i] = building.getElevator(i);
            elevatorButtonsPushed[i] = elevatorArray[i].getButtonStates();
            evaluateAndMove(elevatorArray[i].getElevatorNumber(), elevatorArray[i].getCurrentFloor(),
                    waitingAreaUpPushed, waitingAreaDownPushed, elevatorButtonsPushed[i],
                    elevatorArray[i].getButtonToAnswerAtNextStop());
        }
    }

    private void evaluateAndMove(int elevatorNumber, int currentFloor, boolean[] upPushed, boolean[] downPushed,
                                 boolean[] floorsPushed, Elevator.DirectionEnum direction){

//        System.out.printf("Elevator %d, Floor %d, Moving %s, with these buttons pushed:\n%s\n" +
//                "Waiting to go up: %s\nWaiting to go down: %s\n\n", elevatorNumber, currentFloor,
//                String.valueOf(direction), Arrays.toString(floorsPushed), Arrays.toString(upPushed),
//                Arrays.toString(downPushed));
    }

    public void cycleElapsed(Building building) {
        switch(cycleSwitch){
            case INITIALIZE:
                numElevators = building.getNumberOfElevators();
                numFloors = building.getNumberOfFloors();
                topFloor = numFloors - 1;
                bottomFloor = 0;
                waitingAreaUpPushed = new boolean[numFloors];
                waitingAreaDownPushed = new boolean[numFloors];
                elevatorArray = new Elevator[numElevators];
                elevatorButtonsPushed = new boolean[numElevators][numFloors];
                cycleSwitch = CycleCases.COMPUTE;
            case COMPUTE:
                getWaitingAreaAndElevatorButtons(building);

                break;
        }
    }
}