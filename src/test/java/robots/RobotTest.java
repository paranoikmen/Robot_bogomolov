package robots;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robots.event.RobotActionEvent;
import robots.event.RobotActionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private enum EVENT {ROBOT_MOVED, ROBOT_SKIP_STEP}

    private List<EVENT> events = new ArrayList<>();
    private List<EVENT> expectedEvents = new ArrayList<>();

    private class EventsListener implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            events.add(EVENT.ROBOT_MOVED);
        }

        @Override
        public void robotSkippedStep(@NotNull RobotActionEvent event) {
            events.add(EVENT.ROBOT_SKIP_STEP);
        }

        @Override
        public void robotActivityChanged(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }
    }

    private Cell cell;
    private Cell neighborCell;
    private final Direction direction = Direction.NORTH;

    private Robot robot;

    @BeforeEach
    public void testSetup() {
        // clean events
        events.clear();
        expectedEvents.clear();

        // create robot
        robot = new PlayerRobot();
        robot.setActive(true);
        robot.addRobotActionListener(new EventsListener());

        // create field
        cell = new Cell();
        neighborCell = new Cell();
        cell.setNeighbor(neighborCell, direction);
    }

    @Test
    public void test_setActiveAndIsActive() {
        robot.setActive(true);

        assertTrue(robot.isActive());
        assertTrue(events.isEmpty());
    }


    @Test
    public void test_skipStep_robotActive() {
        cell.setRobot(robot);

        robot.skipStep();

        expectedEvents.add(EVENT.ROBOT_SKIP_STEP);

        assertEquals(cell, robot.getPosition());
        assertEquals(robot, cell.getRobot());
        assertEquals(expectedEvents, events);
    }


}
