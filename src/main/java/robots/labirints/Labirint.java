package robots.labirints;

import org.jetbrains.annotations.NotNull;
import robots.Field;
import robots.Point;

public abstract class Labirint {
    public Field buildField() {

        Field field = new Field(fieldWidth(), fieldHeight(), exitPoint());

        addRobots(field);
        addWalls(field);

        return field;
    }

    protected abstract int fieldHeight();

    protected abstract int fieldWidth();

    protected abstract Point exitPoint();

    protected abstract void addRobots(@NotNull Field field);

    protected abstract void addWalls(@NotNull Field field);

}
