package com.nodalexchange.elevator.controllers;

import com.nodalexchange.elevator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
    private LinkedList<Integer>[] upLinkedArray;
    private LinkedList<Integer>[] downLinkedArray;

    private void getWaitingAreaAndElevatorButtons(Building building){
        for(int i = 0; i < numFloors; i++){
            waitingAreaUpPushed[i] =  building.getElevatorWaitingAreaInfo(i).isUpButtonPushed();
            waitingAreaDownPushed[i] = building.getElevatorWaitingAreaInfo(i).isDownButtonPushed();
            upLinkedArray[i] = new LinkedList<>();
            downLinkedArray[i] = new LinkedList<>();
        }
        for(int i = 0; i < numElevators; i++){
            elevatorArray[i] = building.getElevator(i);
            elevatorButtonsPushed[i] = elevatorArray[i].getButtonStates();
            evaluateAndMove(elevatorArray[i], elevatorArray[i].getCurrentFloor(),
                    waitingAreaUpPushed, waitingAreaDownPushed, elevatorButtonsPushed[i],
                    elevatorArray[i].getButtonToAnswerAtNextStop());
        }
    }

    /**
     * Handles elevator movement of given elevator parameter by setting new instructions. Uses elevator floor buttons
     * pushed, lobby call buttons pushed, and current movement of elevator to decide next move to make.
     * <p>
     * The elevator wants to move in its current direction until empty, stopping to let people off or to pick up people
     * wanting to go in the same direction. So first check the current direction of movement, check the buttons
     *
     * @param elevator Elevator object to be given new instruction
     * @param currentFloor resident floor of elevator
     * @param upPushed array holding true for lobby floors with up buttons pushed
     * @param downPushed array holding true for lobby floors with down buttons pushed
     * @param floorsPushed array holding true for elevator's floor buttons pushed
     * @param currentDirection elevator's previous direction of movement
     * @return void
     */
    private void evaluateAndMove(Elevator elevator, int currentFloor, boolean[] upPushed,
                                 boolean[] downPushed, boolean[] floorsPushed, Elevator.DirectionEnum currentDirection){
        int nextFloor = -1;
        Elevator.DirectionEnum nextDirection = currentDirection;
        if(currentFloor == topFloor){ //on the top floor
            nextDirection = Elevator.DirectionEnum.DOWN;
            for(int i = topFloor; i > bottomFloor; i--){
                if(downPushed[i]){
                    nextFloor = i;
                    break;
                }
            }
            if(nextFloor == -1){
                nextDirection = Elevator.DirectionEnum.UP;
                for(int i = bottomFloor; i < topFloor; i++){
                    if(upPushed[i]){
                        nextFloor = i;
                        break;
                    }
                }
            }
            if(nextFloor == -1){
                elevator.setNewInstruction(currentFloor, currentDirection);
            }
        } else if(currentFloor == bottomFloor){ //on the bottom floor
            nextDirection = Elevator.DirectionEnum.UP;
            for(int i = bottomFloor; i < topFloor; i++){
                if(upPushed[i]){
                    nextFloor = i;
                    break;
                }
            }
            if(nextFloor == -1){
                nextDirection = Elevator.DirectionEnum.DOWN;
                for(int i = topFloor; i > bottomFloor; i--){
                    if(downPushed[i]){
                        nextFloor = i;
                        break;
                    }
                }
            }
            if(nextFloor == -1){
                elevator.setNewInstruction(currentFloor, currentDirection);
            }
        } else{ //somewhere in between

        }

        //
        elevator.setNewInstruction(nextFloor, nextDirection);
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
                upLinkedArray = new LinkedList[numElevators];
                downLinkedArray = new LinkedList[numElevators];
                cycleSwitch = CycleCases.COMPUTE;
            case COMPUTE:
                getWaitingAreaAndElevatorButtons(building);

                break;
        }
    }
}