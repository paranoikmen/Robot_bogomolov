package robots;

import org.jetbrains.annotations.NotNull;
import robots.event.RobotActionEvent;
import robots.event.RobotActionListener;

import java.util.ArrayList;

public abstract class Robot {
    private Cell position;
    private boolean II;

    private boolean isActive;

    public Robot() {

    }

    public Cell getPosition() {
        return position;
    }

    boolean setPosition(Cell position) {
        if (position != null && !canLocateAtPosition(position)) return false;
        if ((position instanceof ExitCell)) return false;
        this.position = position;
        return true;
    }

    public void move(@NotNull Direction direction) {
        if (isActive) {
            Cell oldPosition = position;
            Cell newPosition = canMove(direction);
            if (newPosition != null) {
                fireRobotIsMoved(oldPosition, newPosition);
                position.takeRobot();
                newPosition.setRobot(this);
            }
        }
    }

    public boolean getII() { return II; }

    public void skipStep() {
        if (isActive) {
            fireRobotIsSkipStep();
        }
    }

    void setActive(boolean value) {
        isActive = value;
        fireRobotChangeActive();

    }

    public boolean isActive() {
        return isActive;
    }

    private Cell canMove(@NotNull Direction direction) {
        Cell result = null;

        Cell neighborCell = position.neighborCell(direction);
        if (neighborCell != null && canLocateAtPosition(neighborCell) && position.neighborWall(direction) == null) {
            result = neighborCell;
        }

        return result;
    }

    public static boolean canLocateAtPosition(@NotNull Cell position) {
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

    private void fireRobotIsMoved(@NotNull Cell oldPosition, @NotNull Cell newPosition) {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            event.setFromCell(oldPosition);
            event.setToCell(newPosition);
            listener.robotIsMoved(event);
        }
    }

    private void fireRobotIsSkipStep() {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            listener.robotSkippedStep(event);
        }
    }

    private void fireRobotChangeActive() {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            listener.robotActivityChanged(event);
        }
    }


}
