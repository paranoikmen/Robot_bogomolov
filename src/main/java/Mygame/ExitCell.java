package Mygame;

import Mygame.event.ExitCellActionEvent;
import Mygame.event.ExitCellActionListener;

import java.util.ArrayList;
import java.util.Objects;

public class ExitCell extends Cell {

    // -------------------- События --------------------
    private ArrayList<ExitCellActionListener> exitCellListListener = new ArrayList<>();

    public void addExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.add(listener);
    }

    public void removeExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.remove(listener);
    }

    private void fireRobotIsExit(Robot robot) {
        for(ExitCellActionListener listener: exitCellListListener) {
            ExitCellActionEvent event = new ExitCellActionEvent(listener);
            event.setRobot(robot);
            listener.robotIsExit(event);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExitCell exitCell = (ExitCell) o;
        return Objects.equals(exitCellListListener, exitCell.exitCellListListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), exitCellListListener);
    }

    @Override
    public String toString() {
        return "ExitCell{" +
                "teleportedRobots=" + ", exitCellListListener=" + exitCellListListener +
                '}';
    }
}
