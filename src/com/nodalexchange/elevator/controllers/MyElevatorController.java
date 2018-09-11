package com.nodalexchange.elevator.controllers;

import com.nodalexchange.elevator.*;
import java.util.LinkedList;

public class MyElevatorController implements ElevatorController {
    private enum CycleCases {INITIALIZE, COMPUTE}
    private enum ElevatorState {MOVINGUP, MOVINGDOWN}
    private CycleCases cycleSwitch = CycleCases.INITIALIZE;
    private int numFloors = -1;
    private int numElevators = -1;
    private boolean[] waitingAreaUpPushed;
    private boolean[] waitingAreaDownPushed;
    private Elevator[] elevatorArray;
    private boolean[][] elevatorButtonsPushed;
    private ElevatorState[] elevatorMovementState;
    private LinkedList<Integer>[] floorsToServiceSameDirectionAfter;
    private LinkedList<Integer>[] floorsToServiceOppositeDirection;
    private LinkedList<Integer>[] floorsToServiceSameDirectionBefore;

    private void addValueAscending(int listNum, int eleNum, int val){
        if(listNum == 0 && !floorsToServiceSameDirectionAfter[eleNum].contains(val)){
            int listSize = floorsToServiceSameDirectionAfter[eleNum].size();
            if(listSize == 0){
                floorsToServiceSameDirectionAfter[eleNum].add(val);
            }else if(floorsToServiceSameDirectionAfter[eleNum].get(0) > val){
                floorsToServiceSameDirectionAfter[eleNum].add(0, val);
            }else if(floorsToServiceSameDirectionAfter[eleNum].get(listSize - 1) < val){
                floorsToServiceSameDirectionAfter[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceSameDirectionAfter[eleNum].get(i) < val){
                    i++;
                }
                floorsToServiceSameDirectionAfter[eleNum].add(i, val);
            }
        }else if(listNum == 1 && !floorsToServiceOppositeDirection[eleNum].contains(val)){
            int listSize = floorsToServiceOppositeDirection[eleNum].size();
            if(listSize == 0){
                floorsToServiceOppositeDirection[eleNum].add(val);
            }else if(floorsToServiceOppositeDirection[eleNum].get(0) > val){
                floorsToServiceOppositeDirection[eleNum].add(0, val);
            }else if(floorsToServiceOppositeDirection[eleNum].get(listSize - 1) < val){
                floorsToServiceOppositeDirection[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceOppositeDirection[eleNum].get(i) < val){
                    i++;
                }
                floorsToServiceOppositeDirection[eleNum].add(i, val);
            }
        }else if(listNum == 2 && !floorsToServiceSameDirectionBefore[eleNum].contains(val)){
            int listSize = floorsToServiceSameDirectionBefore[eleNum].size();
            if(listSize == 0){
                floorsToServiceSameDirectionBefore[eleNum].add(val);
            }else if(floorsToServiceSameDirectionBefore[eleNum].get(0) > val){
                floorsToServiceSameDirectionBefore[eleNum].add(0, val);
            }else if(floorsToServiceSameDirectionBefore[eleNum].get(listSize - 1) < val){
                floorsToServiceSameDirectionBefore[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceSameDirectionBefore[eleNum].get(i) < val){
                    i++;
                }
                floorsToServiceSameDirectionBefore[eleNum].add(i, val);
            }
        }
    }

    private void addValueDescending(int listNum, int eleNum, int val){
        if(listNum == 0 && !floorsToServiceSameDirectionAfter[eleNum].contains(val)){
            int listSize = floorsToServiceSameDirectionAfter[eleNum].size();
            if(listSize == 0){
                floorsToServiceSameDirectionAfter[eleNum].add(val);
            }else if(floorsToServiceSameDirectionAfter[eleNum].get(0) < val){
                floorsToServiceSameDirectionAfter[eleNum].add(0, val);
            }else if(floorsToServiceSameDirectionAfter[eleNum].get(listSize - 1) > val){
                floorsToServiceSameDirectionAfter[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceSameDirectionAfter[eleNum].get(i) > val){
                    i++;
                }
                floorsToServiceSameDirectionAfter[eleNum].add(i, val);
            }
        }else if(listNum == 1 && !floorsToServiceOppositeDirection[eleNum].contains(val)){
            int listSize = floorsToServiceOppositeDirection[eleNum].size();
            if(listSize == 0){
                floorsToServiceOppositeDirection[eleNum].add(val);
            }else if(floorsToServiceOppositeDirection[eleNum].get(0) < val){
                floorsToServiceOppositeDirection[eleNum].add(0, val);
            }else if(floorsToServiceOppositeDirection[eleNum].get(listSize - 1) > val){
                floorsToServiceOppositeDirection[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceOppositeDirection[eleNum].get(i) > val){
                    i++;
                }
                floorsToServiceOppositeDirection[eleNum].add(i, val);
            }
        }else if(listNum == 2 && !floorsToServiceSameDirectionBefore[eleNum].contains(val)){
            int listSize = floorsToServiceSameDirectionBefore[eleNum].size();
            if(listSize == 0){
                floorsToServiceSameDirectionBefore[eleNum].add(val);
            }else if(floorsToServiceSameDirectionBefore[eleNum].get(0) < val){
                floorsToServiceSameDirectionBefore[eleNum].add(0, val);
            }else if(floorsToServiceSameDirectionBefore[eleNum].get(listSize - 1) > val){
                floorsToServiceSameDirectionBefore[eleNum].add(listSize, val);
            }else{
                int i = 0;
                while(floorsToServiceSameDirectionBefore[eleNum].get(i) > val){
                    i++;
                }
                floorsToServiceSameDirectionBefore[eleNum].add(i, val);
            }
        }
    }

    private int countTrueElevatorButtons(int eleNum){
        int count = 0;
        for (boolean buttonForFloor: elevatorButtonsPushed[eleNum]) {
            if(buttonForFloor){
                count++;
            }
        }
        return count;
    }

    private void aggregateServicingList(int eleNum, int currentFloor){
        ElevatorState intendedMovement = elevatorMovementState[eleNum];
        if(intendedMovement == ElevatorState.MOVINGUP){
            for(int i = 0; i < numFloors; i++) {
                //iterate through elevatorButtonsPushed, add to ZERO list Ascending
                if (elevatorButtonsPushed[eleNum][i]) {
                    addValueAscending(0, eleNum, i);
                }
                //iterate through waitingAreaUpPushed from currentFloor to topFloor, add to ZERO list Ascending
                if (waitingAreaUpPushed[i] && i >= currentFloor && countTrueElevatorButtons(eleNum) < 3) {
                    addValueAscending(0, eleNum, i);
                }
                //iterate through waitingAreaDownPushed, add to ONE list Descending
                if (waitingAreaDownPushed[i]) {
                    addValueDescending(1, eleNum, i);
                }
                //iterate through waitingAreaUpPushed, add to TWO list Ascending
                if (waitingAreaUpPushed[i] && i < currentFloor) {
                    addValueAscending(2, eleNum, i);
                }
            }
        }
        if(intendedMovement == ElevatorState.MOVINGDOWN){
            for(int i = 0; i < numFloors; i++) {
                //iterate through elevatorButtonsPushed, add to ZERO list Descending
                if (elevatorButtonsPushed[eleNum][i]) {
                    addValueDescending(0, eleNum, i);
                }
                //iterate through waitingAreaDownPushed from currentFloor to topFloor, add to ZERO list Descending
                if (waitingAreaDownPushed[i] && i <= currentFloor && countTrueElevatorButtons(eleNum) < 3) {
                    addValueDescending(0, eleNum, i);
                }
                //iterate through waitingAreaUpPushed, add to ONE list Ascending
                if (waitingAreaUpPushed[i]) {
                    addValueAscending(1, eleNum, i);
                }
                //iterate through waitingAreaDownPushed, add to TWO list Descending
                if (waitingAreaDownPushed[i] && i > currentFloor) {
                    addValueDescending(2, eleNum, i);
                }
            }
        }
        System.out.printf("Elevator %d is ", eleNum);
        System.out.print(intendedMovement);
        System.out.println(" and Servicing List is:");
        System.out.println(floorsToServiceSameDirectionAfter[eleNum]);
        System.out.println(floorsToServiceOppositeDirection[eleNum]);
        System.out.println(floorsToServiceSameDirectionBefore[eleNum]);
    }

    private void getWaitingAreaAndElevatorButtons(Building building){
        for(int i = 0; i < numFloors; i++){
            waitingAreaUpPushed[i] =  building.getElevatorWaitingAreaInfo(i).isUpButtonPushed();
            waitingAreaDownPushed[i] = building.getElevatorWaitingAreaInfo(i).isDownButtonPushed();
        }
        for(int i = 0; i < numElevators; i++){
            elevatorArray[i] = building.getElevator(i);
            elevatorButtonsPushed[i] = elevatorArray[i].getButtonStates();
            evaluateAndMove(elevatorArray[i], elevatorArray[i].getElevatorNumber(), elevatorArray[i].getCurrentFloor(),
                    elevatorMovementState[i]);
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
     * @param elevatorNumber Elevator object's assigned integer id
     * @param currentFloor resident floor of Elevator object
     * @param currentMovementState ElevatorState enum designating current movement state
     */
    private void evaluateAndMove(Elevator elevator, int elevatorNumber, int currentFloor,
                                 ElevatorState currentMovementState){
        aggregateServicingList(elevatorNumber, currentFloor);
        if(floorsToServiceSameDirectionAfter[elevatorNumber].size() != 0){
            int nextFloor = floorsToServiceSameDirectionAfter[elevatorNumber].pop();
            Elevator.DirectionEnum nextButtonAnswer;
            if(currentMovementState == ElevatorState.MOVINGUP){
                nextButtonAnswer = Elevator.DirectionEnum.UP;
            }else{
                nextButtonAnswer = Elevator.DirectionEnum.DOWN;
            }
            elevator.setNewInstruction(nextFloor, nextButtonAnswer);
        }else if(floorsToServiceOppositeDirection[elevatorNumber].size() != 0){
            int nextFloor = floorsToServiceOppositeDirection[elevatorNumber].pop();
            Elevator.DirectionEnum nextButtonAnswer;
            if(currentMovementState == ElevatorState.MOVINGUP){
                nextButtonAnswer =  Elevator.DirectionEnum.DOWN;
                if(nextFloor - currentFloor > 0){
                    elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGUP;
                }else{
                    elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGDOWN;
                }
            }else{
                nextButtonAnswer = Elevator.DirectionEnum.UP;
                if(nextFloor - currentFloor < 0){
                    elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGDOWN;
                }else{
                    elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGUP;
                }
                elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGUP;
            }
            elevator.setNewInstruction(nextFloor, nextButtonAnswer);
        }else if(floorsToServiceSameDirectionBefore[elevatorNumber].size() != 0){
            int nextFloor = floorsToServiceSameDirectionBefore[elevatorNumber].pop();
            Elevator.DirectionEnum nextButtonAnswer;
            if(currentMovementState == ElevatorState.MOVINGUP){
                nextButtonAnswer = Elevator.DirectionEnum.UP;
                elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGDOWN;
            }else{
                nextButtonAnswer = Elevator.DirectionEnum.DOWN;
                elevatorMovementState[elevatorNumber] = ElevatorState.MOVINGUP;
            }
            elevator.setNewInstruction(nextFloor, nextButtonAnswer);
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
                elevatorMovementState = new ElevatorState[numElevators];
                floorsToServiceSameDirectionAfter = new LinkedList[numElevators];
                floorsToServiceOppositeDirection = new LinkedList[numElevators];
                floorsToServiceSameDirectionBefore = new LinkedList[numElevators];
                for(int i = 0; i < numElevators; i++){
                    elevatorMovementState[i] = ElevatorState.MOVINGUP;
                    floorsToServiceSameDirectionAfter[i] = new LinkedList<>();
                    floorsToServiceOppositeDirection[i] = new LinkedList<>();
                    floorsToServiceSameDirectionBefore[i] = new LinkedList<>();
                }
                cycleSwitch = CycleCases.COMPUTE;
            case COMPUTE:
                System.out.println();
                getWaitingAreaAndElevatorButtons(building);
                break;
        }
    }
}