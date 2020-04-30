package Mygame.labirints;

import Mygame.Field;
import Mygame.Point;

public abstract class Labirint {

    protected Field field;

    public Field buildField() {

        field = new Field(fieldWidth(), fieldHeight(), exitPoint());

        addRobots();
        addWalls();

        return field;
    }

    protected abstract int fieldHeight();

    protected abstract int fieldWidth();

    protected abstract Point exitPoint();

    protected abstract void addRobots();

    protected abstract void addWalls();

}
