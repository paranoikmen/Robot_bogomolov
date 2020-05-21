package robots;

import java.util.*;

public class PathFinder {
    private Game game;
    private Field field;
    private int rangeOfSearch = 2;
    private IRobot II;
    private PlayerRobot playerRobot;

    private boolean wayIsReady = false;

    private Cell lastPlayerPosition = null;

    private void init() {
        for(int i = 0; i < field.getRobotsOnField().size(); i++) {
            if(field.getRobotsOnField().get(i).getII()) {
                II = (IRobot) field.getRobotsOnField().get(i);
                ((IRobot) field.getRobotsOnField().get(i)).setPath(this);
            }
            if(!field.getRobotsOnField().get(i).getII()) {
                playerRobot = (PlayerRobot) field.getRobotsOnField().get(i);
            }
        }
    }

    public void setGame(Game game) {
        this.game = game;
        game.setPathFinder(this);
        field = game.getGameField();
        init();
    }
    public Game getGame() { return game; }

    private boolean robotInRange(PlayerRobot robot){

        Cell player = robot.getPosition();

        Cell IIposition = II.getPosition();

        Cell tmp = IIposition;
        Cell cellOfBack;
        Direction directionTurn = Direction.WEST;
        boolean result = false;


        for(int i=0; i<4; i++) {
            if(tmp.neighborCell(directionTurn) == null) {
                directionTurn = directionTurn.getTurningRight();
                i++;
            }
            else {
                cellOfBack = tmp;
                tmp = tmp.neighborCell(directionTurn);

                for(int j = 0; j<rangeOfSearch; j++) {
                    result = tmp.getRobot() != null;
                    if(result) return result;
                    if(tmp.neighborCell(directionTurn.getTurningRight()) != null) {
                        result = tmp.neighborCell(directionTurn.getTurningRight()).getRobot() != null;
                        if(result) return result;
                    }
                    if(tmp.neighborCell(directionTurn.getTurningRight().getOppositeDirection()) != null) {
                        result = tmp.neighborCell(directionTurn.getTurningRight().getOppositeDirection()).getRobot() != null;
                        if(result) return result;
                    }
                    if(tmp.neighborCell(directionTurn) != null) {
                        tmp = tmp.neighborCell((directionTurn));
                    }
                }

                directionTurn = directionTurn.getTurningRight();
            }
        }
        return result;
    }

    public Direction pathFinder() {

        Cell IIposition = II.getPosition();
        Direction result = null;

        if(!robotInRange(playerRobot))
            result = Direction.WEST;
        else {
            //кратчайший путь
            result = path();
        }
        return result;
    }

    public Direction path() {
        Direction direction = null;
        Map<Point, Cell> cells = field.getCells();
        Stack<Cell> queue = new Stack<>();
        queue.push(playerRobot.getPosition());

        Map<Cell, Boolean> used = new HashMap<>();

        Cell tmp1 =null;
        for(var i: cells.entrySet()) {
            tmp1 = i.getValue();
            used.put(tmp1, false);
        }

        Map<Cell, Cell> p = new HashMap<>(); // cell1 = сын, cell2 = предок

        used.put(playerRobot.getPosition(), true);

        p.put(playerRobot.getPosition(),playerRobot.getPosition());

        while(!queue.isEmpty()) {
            Cell tmp = queue.pop();
            Direction directionRight = Direction.WEST;
            for(int i=0; i<4; i++) {
                Cell neighbor = tmp.neighborCell(directionRight);
                if(neighbor != null && neighbor.neighborWall(directionRight) == null) {
                    if(!used.get(neighbor)) {
                        used.put(neighbor, true);
                        queue.push(neighbor);
                        p.put(neighbor, tmp);
                    }
                }
                directionRight = directionRight.getTurningRight();
            }
        }

        Direction abc = null;
        Cell cell1 = null;
        List<Direction> dir = new ArrayList<>();

        if(used.get(II.getPosition()).equals(true)) {
            direction = p.get(II.getPosition()).isNeighbor(II.getPosition());
            cell1 = p.get(II.getPosition());
            abc = p.get(cell1).isNeighbor( cell1);
            for(int i=0; i<30;i++){
                cell1 = p.get(cell1);
                abc = p.get(cell1).isNeighbor(cell1);
                dir.add(abc);
            }
        }

        return direction;
    }


}
