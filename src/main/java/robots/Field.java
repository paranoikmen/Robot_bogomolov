package robots;

import org.jetbrains.annotations.NotNull;
import robots.event.ExitCellActionEvent;
import robots.event.ExitCellActionListener;
import robots.event.FieldActionEvent;
import robots.event.FieldActionListener;

import java.util.*;

public class Field {

    private final Map<Point, Cell> cells = new HashMap<>();

    private final int width;
    private final int height;

    private final Cell exitCell;

    public Field(int width, int height, @NotNull Point exitPoint) {
        if(width <= 0) throw new IllegalArgumentException("Field width must be more than 0");
        if(height <= 0) throw new IllegalArgumentException("Field height must be more than 0");
        if(exitPoint.getX() >= width || exitPoint.getY() >= height)
            throw new IllegalArgumentException("exit point coordinates must be in range from 0 to weight or height");

        this.width = width;
        this.height = height;

        buildField(exitPoint);
        this.exitCell = getCell(exitPoint);

        // Subscribe on exit cell
        ((ExitCell) getCell(exitPoint)).addExitCellActionListener(new ExitCellObserver());
    }

    private void buildField(Point exitPoint) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                Point p = new Point(x, y);
                Cell cell = p.equals(exitPoint)? new ExitCell() : new Cell();
                if(x > 0) cell.setNeighbor(getCell(p.to(Direction.WEST, 1)), Direction.WEST);
                if(y > 0) cell.setNeighbor(getCell(p.to(Direction.NORTH, 1)), Direction.NORTH);
                cells.put(p, cell);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(@NotNull Point point) {
        return cells.get(point);
    }
    public Map<Point, Cell> getCells() {return cells;}
    public List<Robot> getTeleportedRobots() { return ((ExitCell) exitCell).getTeleportedRobots(); }

    public Cell getExitCell() { return exitCell; }

    public List<Robot> getRobotsOnField() {
        List<Robot> robots = new ArrayList<>();
        for(var i : cells.entrySet()) {
            Robot robot = i.getValue().getRobot();
            if(robot != null) robots.add(robot);
        }
        return robots;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return width == field.width &&
                height == field.height &&
                Objects.equals(cells, field.cells) &&
                Objects.equals(exitCell, field.exitCell);
    }


    @Override
    public String toString() {
        return "Field{" +
                "cells=" + cells +
                ", width=" + width +
                ", height=" + height +
                ", exitPoint=" + exitCell +
                '}';
    }

    // -------------------- События --------------------

    class ExitCellObserver implements ExitCellActionListener {

        @Override
        public void robotIsExit(@NotNull ExitCellActionEvent event) {
            fireRobotIsTeleported(event.getRobot(), event.getTeleport());
        }
    }

    private ArrayList<FieldActionListener> fieldListListener = new ArrayList<>();

    public void addFieldlActionListener(FieldActionListener listener) {
        fieldListListener.add(listener);
    }

    public void removeFieldCellActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    private void fireRobotIsTeleported(@NotNull Robot robot, @NotNull Cell teleport) {
        for(FieldActionListener listener: fieldListListener) {
            FieldActionEvent event = new FieldActionEvent(listener);
            event.setRobot(robot);
            event.setTeleport(teleport);
            listener.robotIsExit(event);
        }
    }
}
