
package Mygame;


public class IRobot  extends Robot{

    private int rangeOfSearch = 2;

    private boolean wayIsReady = false;
    private boolean II = true;
    private Cell lastPlayerPosition = null;

    public void move(PlayerRobot robot) {
        Cell player = robot.getPosition();
        Cell II = IRobot.super.getPosition();
        Direction directionToMove = null;

        if(robotInRange(robot) == false && !wayIsReady) {
            if(II.neighborWall(Direction.WEST) == null) move(Direction.WEST);
            else move(Direction.EAST);
        }
        else {
            if(!wayIsReady) {
                lastPlayerPosition = player;
                directionToMove = pathFinder(lastPlayerPosition);
                if(directionToMove != null) wayIsReady = true;
            }
            if(wayIsReady) {
                if(robotInRange(robot) == true) {
                    lastPlayerPosition = player;
                    directionToMove = pathFinder(lastPlayerPosition);
                    move(directionToMove);
                }
                else {
                    directionToMove = pathFinder(lastPlayerPosition);
                    if(directionToMove == null) wayIsReady = false;
                    else move(directionToMove);
                }
            }
        }
    }

    
    private boolean robotInRange(PlayerRobot robot){

        Cell player = robot.getPosition();

        Cell II = IRobot.super.getPosition();

        Cell tmp = II;
        Cell cellOfBack;
        Direction directionTurn = Direction.WEST;
        boolean result = false;

        for(int i=0; i<4; i++) {
            if(tmp.neighborCell(directionTurn) == null ) {
                directionTurn = directionTurn.getTurningRight();
                i++;
            }
            else {
                tmp = tmp.neighborCell(directionTurn);
                cellOfBack = tmp;
                for(int j=0;j<rangeOfSearch;j++) {
                    for(int a=0;a<j+2;a++) {
                        result = tmp.getRobot() !=null;
                        tmp = tmp.neighborCell(directionTurn);
                    }
                    tmp = cellOfBack;
                    for(int a=0;a<j+2;a++) {
                        result = tmp.getRobot() !=null;
                        tmp = tmp.neighborCell(directionTurn.getOppositeDirection());
                    }
                    tmp = cellOfBack;
                    if(tmp.neighborCell(directionTurn) != null) {
                        tmp = tmp.neighborCell(directionTurn);
                    }
                    else {j++;}
                }

            }
        }

        return result;
    }

    public Direction pathFinder(Cell lastRobotCell) {

        Cell II = IRobot.super.getPosition();
        Direction result = null;
        if(II.equals(lastRobotCell))
            result = null;
        else {
            //кратчайший путь
        }
        return result;
    }


}
