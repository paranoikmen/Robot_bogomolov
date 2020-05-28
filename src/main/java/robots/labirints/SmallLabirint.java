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
        return new Point(5,5);
    }

    @Override
    protected void addRobots(@NotNull Field field) {
        PlayerRobot playerRobot = new PlayerRobot();
        IRobot II = new IRobot();

        field.getCell(new Point(0,0)).setRobot(II);
        field.getCell(new Point(0,2)).setRobot(playerRobot);
    }



    @Override
    protected void addWalls(@NotNull Field field) {
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(
                field.getCell(new Point(2, 0)),
                field.getCell(new Point(2, 1))
        );

        new Wall(betweenCellsPosition);

        betweenCellsPosition = new BetweenCellsPosition(
                field.getCell(new Point(1, 0)),
                field.getCell(new Point(1, 1))
        );

        new Wall(betweenCellsPosition);

        betweenCellsPosition = new BetweenCellsPosition(
                field.getCell(new Point(0, 0)),
                field.getCell(new Point(0, 1))
        );

        new Wall(betweenCellsPosition);
    }
}
