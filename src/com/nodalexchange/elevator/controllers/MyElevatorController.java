package com.nodalexchange.elevator.controllers;

import com.nodalexchange.elevator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MyElevatorController implements ElevatorController {
    private enum CycleCases {INITIALIZE, COMPUTE}
    private CycleCases cycleSwitch = CycleCases.INITIALIZE;
    private int numFloors = 0;
    private int numElevators = 0;
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
        }
    }

    public void cycleElapsed(Building building) {
        switch(cycleSwitch){
            case INITIALIZE:
                numElevators = building.getNumberOfElevators();
                numFloors = building.getNumberOfFloors();
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