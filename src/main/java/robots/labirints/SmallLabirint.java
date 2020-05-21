package robots.labirints;

import org.jetbrains.annotations.NotNull;
import robots.*;

public class SmallLabirint extends Labirint {

    private static final int FIELD_HEIGHT = 6;
    private static final int FIELD_WIDTH = 6;


    @Override
    protected int fieldHeight() {
        return FIELD_HEIGHT;
    }

    @Override
    protected int fieldWidth() {
        return FIELD_WIDTH;
    }

    @Override
    protected Point exitPoint() {
        return new Point(2,2);
    }

    @Override
    protected void addRobots(@NotNull Field field) {
        PlayerRobot firstRobot = new PlayerRobot();
        IRobot secondRobot = new IRobot();

        field.getCell(new Point(0,2)).setRobot(firstRobot);
        field.getCell(new Point(2,0)).setRobot(secondRobot);
    }



    @Override
    protected void addWalls(@NotNull Field field) {
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(
                field.getCell(new Point(2, 0)),
                field.getCell(new Point(2, 1))
        );

        new Wall(betweenCellsPosition);
    }
}
