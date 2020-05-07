package Mygame;

import org.jetbrains.annotations.NotNull;
import Mygame.event.RobotActionEvent;
import Mygame.event.RobotActionListener;

import java.util.ArrayList;

public abstract class Robot {

    private Cell position;

    private boolean isActive;
    private boolean II;

    public Cell getPosition() {
        return position;
    }

    void setPosition(Cell position) {
        this.position = position;
    }

    public void move(@NotNull Direction direction) {
        if(isActive) {
            Cell newPosition = canMove(direction);
            if (newPosition != null) {
                fireRobotIsMoved();
                position.takeRobot();
                newPosition.setRobot(this);
            }
        }
    }

    public boolean getII(){return II;}

    public void skipStep() {
        if(isActive) {
           fireRobotIsSkipStep();
        }
    }

    void setActive(boolean value) {
        isActive = value;
    }

    public boolean isActive() {
        return isActive;
    }


    private Cell canMove(@NotNull Direction direction) {
        Cell result = null;

        Cell neighborCell = position.neighborCell(direction);
        if(neighborCell != null && canStayAtPosition(neighborCell) && position.neighborWall(direction) == null) {
            result = neighborCell;
        }

        return result;
    }


    public static boolean canStayAtPosition(@NotNull Cell position) {
        return position.getRobot() == null;
    }

    // -------------------- События --------------------
    private ArrayList<RobotActionListener> robotListListener = new ArrayList<>();

    public void addRobotActionListener(RobotActionListener listener) {
        robotListListener.add(listener);
    }

    public void removeRoborActionListener(RobotActionListener listener) {
        robotListListener.remove(listener);
    }

    private void fireRobotIsMoved() {
        for(RobotActionListener listener: robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            listener.robotIsMoved(event);
        }
    }

    private void fireRobotIsSkipStep() {
        for(RobotActionListener listener: robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            listener.robotIsSkipStep(event);
        }
    }
}
