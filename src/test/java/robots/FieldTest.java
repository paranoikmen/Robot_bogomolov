package robots;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robots.event.FieldActionEvent;
import robots.event.FieldActionListener;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private int eventCount = 0;

    class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsExit(@NotNull FieldActionEvent event) {

        }
    }

    private Field field;

    @BeforeEach
    public void testSetup() {
        eventCount = 0;
        field = new Field(2, 2, new Point(1, 1));
        field.addFieldlActionListener(new FieldObserver());
    }

    @Test
    public void test_create_withCorrectParams() {
        Cell cell_0_0 = field.getCell(new Point(0, 0));
        Cell cell_0_1 = field.getCell(new Point(1, 0));
        Cell cell_1_0 = field.getCell(new Point(0, 1));
        Cell cell_1_1 = field.getCell(new Point(1, 1));

        assertEquals(Direction.SOUTH, cell_0_0.isNeighbor(cell_1_0));
        assertEquals(Direction.SOUTH, cell_0_1.isNeighbor(cell_1_1));
        assertEquals(Direction.NORTH, cell_1_1.isNeighbor(cell_0_1));
        assertEquals(Direction.NORTH, cell_1_0.isNeighbor(cell_0_0));
        assertEquals(Direction.EAST, cell_0_0.isNeighbor(cell_0_1));
        assertEquals(Direction.EAST, cell_1_0.isNeighbor(cell_1_1));
        assertEquals(Direction.WEST, cell_0_1.isNeighbor(cell_0_0));
        assertEquals(Direction.WEST, cell_1_1.isNeighbor(cell_1_0));
        assertTrue(cell_1_1 instanceof ExitCell);
    }

    @Test
    public void test_create_withNegativeWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(-1, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(0, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withNegativeHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, -1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 0, new Point(0, 0)));
    }

    @Test
    public void test_create_withIncorrectExitPoint() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 1, new Point(2, 2)));
    }

    @Test
    public void test_getRobotsOnField_empty() {
        assertTrue(field.getRobotsOnField().isEmpty());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() {
        Robot robot = new PlayerRobot();
        field.getCell(new Point(0, 0)).setRobot(robot);

        assertTrue(field.getRobotsOnField().contains(robot));
        assertEquals(1, field.getRobotsOnField().size());
    }

    @Test
    public void test_getRobotsOnField_severalRobots() {
        Robot robot = new PlayerRobot();
        Robot anotherRobot = new IRobot();
        field.getCell(new Point(0, 0)).setRobot(robot);
        field.getCell(new Point(1, 0)).setRobot(anotherRobot);

        assertTrue(field.getRobotsOnField().containsAll(Arrays.asList(robot, anotherRobot)));
        assertEquals(2, field.getRobotsOnField().size());
    }
}