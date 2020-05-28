package robots.labirint;

import org.jetbrains.annotations.NotNull;
import robots.*;
import robots.labirints.Labirint;

public class TestLabirint extends Labirint {

    private static final int FIELD_HEIGHT = 3;
    private static final int FIELD_WIDTH = 3;
    private static final int DEFAULT_BATTERY_CHARGE = 10;

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

    }

    @Override
    protected void addWalls(@NotNull Field field) {

    }


}
