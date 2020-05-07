package Mygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private Cell cell;

    public CellTest() {
    }


    @BeforeEach
    public void testSetup() {

        cell = new Cell();
    }

    @Test
    public void test_setRobot_InEmptyCell() {
        Robot robot = new PlayerRobot();

        cell.setRobot(robot);

        assertEquals(robot, cell.getRobot());
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_takeRobot_FromCellWithRobot() {
        Robot robot = new PlayerRobot();

        cell.setRobot(robot);

        assertEquals(robot, cell.takeRobot());
        assertNull(robot.getPosition());
        assertNull(cell.getRobot());
    }


    @Test
    public void test_setRobot_ToCellWithRobot() {
        Robot robot = new PlayerRobot();
        Robot newRobot = new PlayerRobot();

        cell.setRobot(robot);

        assertThrows(IllegalArgumentException.class, () -> cell.setRobot(newRobot));
        assertEquals(robot, cell.getRobot());
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }



    @Test
    public void test_setNeighborCell() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);

        assertEquals(neighborCell, cell.neighborCell(direction));
        assertEquals(cell, neighborCell.neighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        neighborCell.setNeighbor(cell, direction.getOppositeDirection());
        assertEquals(neighborCell, cell.neighborCell(direction));
        assertEquals(cell, neighborCell.neighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() {
        Cell neighborCell = new Cell();
        Cell anotherCell = new Cell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        assertThrows(IllegalArgumentException.class, () -> cell.setNeighbor(anotherCell, direction));
        assertEquals(neighborCell, cell.neighborCell(direction));
        assertEquals(cell, neighborCell.neighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        assertEquals(direction, cell.isNeighbor(neighborCell));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellNotExists() {
        Cell neighborCell = new Cell();

        assertNull(cell.isNeighbor(neighborCell));
    }

    @Test
    public void test_setNeighbor_Wall() {
        Direction direction = Direction.NORTH;
        Wall neighborWall = new Wall(new BetweenCellsPosition(cell, direction));

        assertEquals(neighborWall, cell.neighborWall(direction));
        assertEquals(cell, neighborWall.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(cell.neighborWall(direction));
    }
}