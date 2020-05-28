package robots;

public class IRobot  extends Robot {
    private boolean II = true;
    private int rangeOfSearch = 2;
    private boolean wayIsReady = false;
    private PathFinder pathFinder;
    private boolean left = true;

    public void setPathFinder(PathFinder pathFinder) { this.pathFinder = pathFinder; }

    public void move() {
        Cell IIposition = this.getPosition();
        Direction result = null;

        if(!robotInRange()) {
            if (wayIsReady) {
                if(pathFinder.sizePath()!=0){
                    result = pathFinder.popLast();
                }
                else {
                    wayIsReady = false;
                }
            }
            if(!wayIsReady) {
                if(IIposition.neighborCell(Direction.WEST)==null) { left = false; }
                else if(IIposition.neighborCell(Direction.EAST)==null) { left = true; }
                if(left) { result = Direction.WEST; }
                else result = Direction.EAST;
            }
        }
        else {
            pathFinder.rewrite();
            result = pathFinder.path();
            wayIsReady = true;
        }
        if(result == null) skipStep();
        else move(result);
    }

    private boolean robotInRange(){
        Cell IIposition = this.getPosition();

        Cell tmp = IIposition;
        Direction directionTurn = Direction.WEST;
        boolean result = false;


        for(int i=0; i<4; i++) {
            if(tmp.neighborCell(directionTurn) != null) {
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
                tmp = IIposition;
            }
            directionTurn = directionTurn.getTurningRight();
        }
        return result;
    }

    @Override
    public boolean getII() { return II; }
}
