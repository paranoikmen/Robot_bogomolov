package Mygame.labirints;

import Mygame.*;

public class SmallLabirint extends Labirint {

    private static final int FIELD_HEIGHT = 3;
    private static final int FIELD_WIDTH = 3;


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
    protected void addRobots() {
        Robot playerRobot = new PlayerRobot();

        Robot IIRobot = new IRobot();


        field.getCell(new Point(0,2)).setRobot(playerRobot);
        field.getCell(new Point(2,0)).setRobot(IIRobot);
    }



    @Override
    protected void addWalls() {
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(
                field.getCell(new Point(2, 0)),
                field.getCell(new Point(2, 1))
        );

        new Wall(betweenCellsPosition);
    }
}
