package Mygame.ui;

import org.jetbrains.annotations.NotNull;
import Mygame.*;
import Mygame.Robot;
import Mygame.ui.block.WallWidget;
import Mygame.ui.cell.CellWidget;
import Mygame.ui.cell.ExitWidget;
import Mygame.ui.cell.RobotWidget;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<Robot, RobotWidget> robots = new HashMap<>();
    private final Map<Wall, WallWidget> walls = new HashMap<>();
    private final List<Color> usedColors = new ArrayList<>();

    public CellWidget create(@NotNull Cell cell) {
        if(cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = (cell instanceof ExitCell) ? new ExitWidget() : new CellWidget();

        Robot robot = cell.getRobot();
        if(robot != null) {
            RobotWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        cells.put(cell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull Cell cell) {
        return cells.get(cell);
    }

    public RobotWidget create(@NotNull Robot robot) {
        if(robots.containsKey(robot)) return robots.get(robot);

        Color color = (usedColors.contains(Color.RED)) ? Color.BLUE : Color.RED;
        usedColors.add(color);

        RobotWidget item = new RobotWidget(robot, color);
        robots.put(robot, item);
        return item;
    }

    public RobotWidget getWidget(@NotNull Robot robot) {
        return robots.get(robot);
    }



    public WallWidget create(@NotNull Wall wall, Orientation orientation) {
        if(walls.containsKey(wall)) return walls.get(wall);

        WallWidget item = new WallWidget(orientation);
        walls.put(wall, item);
        return item;
    }

    public WallWidget getWidget(@NotNull Wall wall) {
        return walls.get(wall);
    }
}