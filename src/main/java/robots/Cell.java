package robots;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Cell {

    private UUID uuid = UUID.randomUUID();

    /**
     * Robot
     */
    private Robot robot;

    public Robot getRobot() {
        return robot;
    }

    public Robot takeRobot() {
        robot.setPosition(null);
        var tmp = robot;
        robot = null;
        return tmp;
    }

    public void setRobot(Robot robot) {
        boolean isPositionSetSuccess = robot.setPosition(this);
       // if(!isPositionSetSuccess) throw new IllegalArgumentException("In cell already set robot");
        this.robot = robot;
    }

    /**
     * Neighbor cells
     */
    private Map<Direction, Cell> neighborCells = new EnumMap<>(Direction.class);

    public Cell neighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    void setNeighbor(@NotNull Cell neighborCell, @NotNull Direction direction) {
        if(neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell)) throw new IllegalArgumentException();
        neighborCells.put(direction, neighborCell);
        if(neighborCell.neighborCell(direction.getOppositeDirection()) == null) {
            neighborCell.setNeighbor(this, direction.getOppositeDirection());
        }
    }

    public Direction isNeighbor(@NotNull Cell other) {
        for(var i : neighborCells.entrySet()) {
            if(i.getValue().equals(other)) return i.getKey();
        }
        return null;
    }

    /**
     * Neighbor walls
     */
    private Map<Direction, Wall> neighborWalls = new EnumMap<>(Direction.class);

    public Wall neighborWall(@NotNull Direction direction) {
        return neighborWalls.get(direction);
    }

    void setNeighbor(@NotNull Wall wall, @NotNull Direction direction) {
        neighborWalls.put(direction, wall);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(robot, cell.robot) &&
                Objects.equals(uuid, cell.uuid) &&
                Objects.equals(neighborCells, cell.neighborCells) &&
                Objects.equals(neighborWalls, cell.neighborWalls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, neighborCells.size(), neighborWalls.size());
    }

    @Override
    public String toString() {
        return "Cell{" +
                ", robot=" + robot +
                ", neighborCells=" + neighborCells.size() +
                ", neighborWalls=" + neighborWalls.size() +
                '}';
    }
}
