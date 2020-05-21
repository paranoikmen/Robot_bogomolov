package robots.ui;

import org.jetbrains.annotations.NotNull;
import robots.*;
import robots.event.FieldActionEvent;
import robots.event.FieldActionListener;
import robots.event.RobotActionEvent;
import robots.event.RobotActionListener;
import robots.ui.block.BetweenCellsWidget;
import robots.ui.block.WallWidget;
import robots.ui.cell.CellWidget;
import robots.ui.cell.RobotWidget;

import javax.swing.*;
import java.util.List;

public class FieldWidget extends JPanel {

    private final Field field;
    private final WidgetFactory widgetFactory;

    public FieldWidget(@NotNull Field field, @NotNull WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        fillField();
        subscribeOnRobots();
        field.addFieldlActionListener(new FieldController());
    }

    private void fillField() {
        if(field.getHeight() > 0) {
            JPanel startRowWalls = createRowWalls(0, Direction.NORTH);
            add(startRowWalls);
        }

        for (int i = 0; i < field.getHeight(); ++i) {
            JPanel row = createRow(i);
            add(row);
            JPanel rowWalls = createRowWalls(i, Direction.SOUTH);
            add(rowWalls);
        }
    }

    private JPanel createRow(int rowIndex) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);
            CellWidget cellWidget = widgetFactory.create(cell);

            if(i == 0)  {
                BetweenCellsWidget westCellWidget = new BetweenCellsWidget(Orientation.VERTICAL);
                Wall wallSegment = cell.neighborWall(Direction.WEST);
                if( wallSegment != null) {
                    WallWidget wallWidget = widgetFactory.create(wallSegment, Orientation.VERTICAL);
                    westCellWidget.addItem(wallWidget);
                }
                row.add(westCellWidget);
            }

            row.add(cellWidget);

            BetweenCellsWidget eastCellWidget = new BetweenCellsWidget(Orientation.VERTICAL);
            Wall eastWallSegment = cell.neighborWall(Direction.EAST);
            if(eastWallSegment != null) {
                WallWidget wallWidget = widgetFactory.create(eastWallSegment, Orientation.VERTICAL);
                eastCellWidget.addItem(wallWidget);
            }

            row.add(eastCellWidget);
        }
        return row;
    }

    private JPanel createRowWalls(int rowIndex, Direction direction) {
        if(direction == Direction.EAST || direction == Direction.WEST) throw new IllegalArgumentException();
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);

            BetweenCellsWidget southCellWidget = new BetweenCellsWidget(Orientation.HORIZONTAL);
            Wall southWallSegment = cell.neighborWall(direction);

            if(southWallSegment != null) {
                WallWidget wallWidget = widgetFactory.create(southWallSegment, Orientation.HORIZONTAL);
                southCellWidget.addItem(wallWidget);
            }

            row.add(southCellWidget);
        }
        return row;
    }

    private void subscribeOnRobots() {
        List<Robot> robots = field.getRobotsOnField();
        for(Robot robot : robots) {
            robot.addRobotActionListener(new RobotController());
        }
    }

    private class RobotController implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            RobotWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            CellWidget to = widgetFactory.getWidget(event.getToCell());
            from.removeItem(robotWidget);
            to.addItem(robotWidget);
        }

        @Override
        public void robotSkippedStep(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            RobotWidget robotWidget = widgetFactory.getWidget(robot);
            robotWidget.repaint();
        }

        @Override
        public void robotActivityChanged(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            RobotWidget robotWidget = widgetFactory.getWidget(robot);
            robotWidget.setActive(robot.isActive());
        }


    }

    private class FieldController implements FieldActionListener {

        @Override
        public void robotIsExit(@NotNull FieldActionEvent event) {
            Robot robot = event.getRobot();
            Cell teleport = event.getTeleport();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            RobotWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }
    }
}
