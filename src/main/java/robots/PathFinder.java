package robots;

import java.util.*;

public class PathFinder {
    private Field field;
    private IRobot II;
    private PlayerRobot playerRobot;
    Stack<Direction> pullDirection = new Stack<>();
    private Cell lastPlayerPosition;

    public void rewrite() {
        for(int i = 0; i < field.getRobotsOnField().size(); i++) {
            if(field.getRobotsOnField().get(i).getII()) {
                II = (IRobot) field.getRobotsOnField().get(i);
                ((IRobot) field.getRobotsOnField().get(i)).setPathFinder(this);
            }
            if(!field.getRobotsOnField().get(i).getII()) {
                playerRobot = (PlayerRobot) field.getRobotsOnField().get(i);
                setLastPlayerPosition(playerRobot.getPosition());
            }
        }
    }

    public void setField(Field field) {
        this.field = field;
        field.setPathFinder(this);
        rewrite();
    }

    public Field getField() { return field; }
    public PlayerRobot getPlayerRobot() { return playerRobot; }
    public IRobot getII() { return II; }
    public Cell getLastPlayerPosition() { return lastPlayerPosition; }
    public Direction popLast() { return pullDirection.pop(); }
    public Direction peekLast() { return pullDirection.peek(); }
    public void setLastPlayerPosition(Cell cell) { lastPlayerPosition = cell; }
    public int sizePath() { return pullDirection.size(); }

    public Direction path() {
        Map<Point, Cell> cells = field.getCells();
        Stack<Cell> queue = new Stack<>();
        Map<Cell, Boolean> used = new HashMap<>();
        Map<Cell, Cell> p = new HashMap<>(); // cell1 = сын, cell2 = предок

        for(var i: cells.entrySet()) {
            used.put(i.getValue(), false);
        }

        used.put(lastPlayerPosition, true);
        queue.push(lastPlayerPosition);
        p.put(lastPlayerPosition,lastPlayerPosition);

        while(p.get(II.getPosition())==null) {
            Cell tmp = queue.pop();
            Direction directionRight = Direction.SOUTH;
            for(int i=0; i<4; i++) {
                Cell neighbor = tmp.neighborCell(directionRight);
                if(neighbor != null && tmp.neighborWall(directionRight) == null) {
                    if(!used.get(neighbor)) {
                        used.put(neighbor, true);
                        queue.push(neighbor);
                        p.put(neighbor, tmp);
                    }
                }
                directionRight = directionRight.getTurningRight();
            }
        }


        Direction direction = null;
        Stack<Direction> reverseStack = new Stack<>();
        Cell tmpCell = null;

        if(used.get(II.getPosition()).equals(true)) {
            direction = II.getPosition().isNeighbor(p.get(II.getPosition()));

            tmpCell = p.get(II.getPosition());
            reverseStack.push(tmpCell.isNeighbor(p.get(tmpCell)));

            for(int i = 0;  reverseStack.peek()!=null ; i++) {
                tmpCell = p.get(tmpCell);
                reverseStack.push(tmpCell.isNeighbor(p.get(tmpCell)));
            }
            for(;reverseStack.size()!=0;) {
                if(reverseStack.peek()==null) { reverseStack.pop(); }
                if(reverseStack.peek()!=null){
                    pullDirection.push(reverseStack.pop());
                }
            }
        }

        return direction;
    }


}
