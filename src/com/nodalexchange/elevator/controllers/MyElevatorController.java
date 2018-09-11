package com.nodalexchange.elevator.controllers;

import com.nodalexchange.elevator.*;
import java.util.LinkedList;

/** Represents an ElevatorController
 * @author Anup Jasani
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class MyElevatorController implements ElevatorController {
    private enum CycleCases {INITIALIZE, COMPUTE}
    private enum ElevatorState {MOVINGUP, MOVINGDOWN}
    private CycleCases cycleSwitch = CycleCases.INITIALIZE;
    private int numFloors = -1;
    private int numElevators = -1;
    private boolean[] waitingAreaUpPushed;
    private boolean[] waitingAreaDownPushed;
    private boolean[][] elevatorButtonsPushed;
    private ElevatorState[] elevatorMovementState;
    private LinkedList<Integer>[] floorsToServiceSameDirectionAfter;
    private LinkedList<Integer>[] floorsToServiceOppositeDirection;
    private LinkedList<Integer>[] floorsToServiceSameDirectionBefore;

    /** Adds the value to the LinkedList specified in ascending order
     *
     * @param listNum number of the corresponding LinkedList
     * @param eleNum number of the Elevator object
     * @param val value being added to the LinkedList
     */
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

    /** Adds the value to the LinkedList specified in descending order
     *
     * @param listNum number of the corresponding LinkedList
     * @param eleNum number of the Elevator object
     * @param val value being added to the LinkedList
     */
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

    /** Counts the number of times true appears in the boolean[] elevatorButtonsPushed
     *
     * @param eleNum number of the Elevator object
     * @return the integer count of boolean true in the array
     */
    private int countTrueElevatorButtons(int eleNum){
        int count = 0;
        for (boolean buttonForFloor: elevatorButtonsPushed[eleNum]) {
            if(buttonForFloor){
                count++;
            }
        }
        return count;
    }

    /** Depending on the current movement of the elevator and the floor that it resides on, 3 LinkedLists will be added
     * to. The first will take all elevator buttons pushed and all lobby call floors on the current floor and the
     * floors to come in its direction. The second will take all the the lobby call floors going in the opposite
     * direction. And the third will take all of the lobby call floors going in the same direction but preceding the
     * current floor.
     * <p>
     * Each of these LinkedLists are added to in an ascending of descending fashion, depending on the current direction.
     *
     * @param elevator Elevator object
     * @param eleNum number of the Elevator object
     * @param currentFloor current floor the Elevator object is on
     */
    private void aggregateServicingList(Elevator elevator, int eleNum, int currentFloor){
        ElevatorState intendedMovement = elevatorMovementState[eleNum];
        int capacity = elevator.getCapacity();
        if(intendedMovement == ElevatorState.MOVINGUP){
            for(int i = 0; i < numFloors; i++) {
                //iterate through elevatorButtonsPushed, add to ZERO list Ascending
                if (elevatorButtonsPushed[eleNum][i]) {
                    addValueAscending(0, eleNum, i);
                }
                //iterate through waitingAreaUpPushed from currentFloor to topFloor, add to ZERO list Ascending
                if (waitingAreaUpPushed[i] && i >= currentFloor && countTrueElevatorButtons(eleNum) < capacity/3) {
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
                if (waitingAreaDownPushed[i] && i <= currentFloor && countTrueElevatorButtons(eleNum) < capacity/3) {
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
    }

    /** Puts lobby button data into boolean[] and grabs essential Elevator data.
     *
     * @param building Building object that holds the data for the lobby call buttons
     */
    private void getWaitingAreaAndElevatorButtons(Building building){
        Elevator e;
        for(int i = 0; i < numFloors; i++){
            waitingAreaUpPushed[i] =  building.getElevatorWaitingAreaInfo(i).isUpButtonPushed();
            waitingAreaDownPushed[i] = building.getElevatorWaitingAreaInfo(i).isDownButtonPushed();
        }
        for(int i = 0; i < numElevators; i++){
            e = building.getElevator(i);
            elevatorButtonsPushed[i] = e.getButtonStates();
            evaluateAndMove(e, e.getElevatorNumber(), e.getCurrentFloor(),
                    elevatorMovementState[i]);
        }
    }

    /** Handles elevator movement of given elevator parameter by setting new instructions. Will check up to three of the
     * given LinkedLists and based on the one it grabs data from the elevator will move in that style.
     * <p>
     * The elevator wants to move in its current direction until empty, stopping to let people off or to pick up people
     * wanting to go in the same direction.
     *
     * @param elevator Elevator object to be given new instruction
     * @param elevatorNumber Elevator object's assigned integer id
     * @param currentFloor resident floor of Elevator object
     * @param currentMovementState ElevatorState enum designating current movement state
     */
    private void evaluateAndMove(Elevator elevator, int elevatorNumber, int currentFloor,
                                 ElevatorState currentMovementState){
        aggregateServicingList(elevator, elevatorNumber, currentFloor);
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

    /** Gets called in SystemLauncher.java after elevator has moved or is loading.
     * <p>
     * During only the first call all of the private globals are initialized/instantiated to the appropriate sizes and
     * values. Every other time getWaitingAreaAndElevatorButtons() gets called.
     *
     * @param building Building object that the cycle is being evaluated on
     */
    public void cycleElapsed(Building building) {
        switch(cycleSwitch){
            case INITIALIZE:
                numElevators = building.getNumberOfElevators();
                numFloors = building.getNumberOfFloors();
                waitingAreaUpPushed = new boolean[numFloors];
                waitingAreaDownPushed = new boolean[numFloors];
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
                getWaitingAreaAndElevatorButtons(building);
                break;
        }
    }
}