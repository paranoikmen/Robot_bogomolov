package Mygame;

public enum Direction {
    NORTH, SOUTH, WEST, EAST;

    private Direction opposite;
    private Direction turning;

    static  {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        WEST.opposite = EAST;
        EAST.opposite = WEST;
    }

    static {
        NORTH.turning = EAST;
        SOUTH.turning = WEST;
        WEST.turning = NORTH;
        EAST.turning = SOUTH;
    }

    public Direction getOppositeDirection() {
        return opposite;
    }
    public Direction getTurningRight() {
        return turning;
    }
}
