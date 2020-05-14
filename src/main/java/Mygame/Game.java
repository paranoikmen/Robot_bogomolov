package Mygame;

import org.jetbrains.annotations.NotNull;
import Mygame.event.*;
import Mygame.labirints.Labirint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Game {

    private GameStatus gameStatus;
    private Robot activeRobot;
    private Robot winner;
    private Field gameField;
    private Labirint labirint;

    public Game(Labirint labirint) {
        this.labirint = labirint;
        initGame();
    }

    private void initGame() {
        gameStatus = GameStatus.GAME_IS_ON;

        buildField();

        gameField.addFieldlActionListener(new FieldObserver());

        for(var i : gameField.getRobotsOnField()) {
            i.addRobotActionListener(new RobotObserver());
        }

        // set active robot
        passMoveNextRobot();
    }

    public void finish() {
        gameStatus = GameStatus.GAME_FINISHED_AHEAD_OF_SCHEDULE;
        setActiveRobot(null);
    }

    public GameStatus status() {
        return gameStatus;
    }

    public Robot winner() {
        return winner;
    }

    public Robot activeRobot() {
        return activeRobot;
    }

    private void passMoveNextRobot() {
        if(gameStatus != GameStatus.GAME_IS_ON) {
            setActiveRobot(null);
            return;
        }

        List<Robot> robotsOnField = gameField.getRobotsOnField();

        if (robotsOnField.size() == 2) {
            Robot firstRobot = robotsOnField.get(0);
            Robot secondRobot = robotsOnField.get(1);
            if(!firstRobot.isActive()) {
                setActiveRobot(firstRobot);
            } else if (!secondRobot.isActive()){
                setActiveRobot(secondRobot);
            }
        }
    }

    private GameStatus determineOutcomeGame() {
        GameStatus result = GameStatus.GAME_IS_ON;

        List<Robot> robotsOnField = gameField.getRobotsOnField();

        Cell exitCell = gameField.getExitCell();

        Robot playerRobot = null;
        Robot IIRobot = null;
        if(robotsOnField.get(0).getII()==false) {
            playerRobot = robotsOnField.get(0);
            IIRobot = robotsOnField.get(1);
        }
        else {
            playerRobot = robotsOnField.get(1);
            IIRobot = robotsOnField.get(0);
        }

        if(exitCell.getRobot().equals(playerRobot) == true) {
            result = GameStatus.WIN_IS_PLAYER;
        }
        else if(playerRobot.getPosition().equals(IIRobot.getPosition()) == true) {
            result = GameStatus.WIN_IS_II;
        }

        return result;
    }


    private void buildField() {
        gameField = labirint.buildField();
    }

    private void setWinner(@NotNull Robot robot) {

        winner = robot;

        setActiveRobot(null);
    }

    private void setActiveRobot(Robot robot) {

        if (activeRobot != null) activeRobot.setActive(false);

        activeRobot = robot;

        if(robot != null ) robot.setActive(true);
    }

    public Direction path() {
        Direction direction = null;

        Map<Point,Cell> cells = gameField.getCells();

        List<Robot> robotsOnField = gameField.getRobotsOnField();
        Cell playerRobot = null;
        Cell IIRobot = null;
        if(robotsOnField.get(0).getII()==false) {
            playerRobot = robotsOnField.get(0).getPosition();
            IIRobot = robotsOnField.get(1).getPosition();
        }
        else {
            playerRobot = robotsOnField.get(1).getPosition();
            IIRobot = robotsOnField.get(0).getPosition();
        }


        Stack<Cell> queue = new Stack<>();

        queue.push(IIRobot);

        Map<Cell, Boolean> used = null;
        for(int i = 0;i<cells.size(); i++){
            used.put(cells.get(i), false);
        }

        Map<Cell, Cell> p = null; // cell1 = сын, cell2 = предок

        used.put(playerRobot, true);

        p.put(playerRobot,playerRobot);

        // todo учитывать стены

        while(!queue.isEmpty()) {
            Cell tmp = queue.pop();
            Direction directionRight = Direction.WEST;
            for(int i=0; i<4; i++) {
                Cell neibhor = tmp.neighborCell(directionRight);
                if(!neibhor.equals(null) && neibhor.neighborWall(directionRight).equals(null)) {
                    if(used.get(neibhor).equals(false)) {
                        used.put(neibhor, true);
                        queue.push(neibhor);
                        p.put(neibhor, playerRobot);
                    }
                }
                direction = directionRight.getTurningRight();
            }
        }

        if(used.get(IIRobot).equals(true)) {
            direction = p.get(IIRobot).isNeighbor(p.get(p.get(IIRobot)));
        }

        return direction;
    }

    /** Events */

    private class RobotObserver implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            fireRobotIsMoved(event.getRobot());

            if(!(event.getRobot().getPosition() instanceof ExitCell)){
                gameStatus = determineOutcomeGame();
                passMoveNextRobot();
            }
        }

        @Override
        public void robotIsSkipStep(@NotNull RobotActionEvent event) {
            fireRobotIsSkipStep(event.getRobot());
            gameStatus = determineOutcomeGame();
            passMoveNextRobot();
        }
    }

    private class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsExit(@NotNull FieldActionEvent event) {
            fireRobotIsExit(event.getRobot());
            gameStatus = determineOutcomeGame();
            passMoveNextRobot();
        }
    }

    private ArrayList<GameActionListener> gameActionListeners = new ArrayList<>();

    public void addGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    public void removeGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.remove(listener);
    }

    private void fireRobotIsMoved(@NotNull Robot robot) {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            event.setRobot(robot);
            listener.robotIsMoved(event);
        }
    }

    private void fireRobotIsSkipStep(@NotNull Robot robot) {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            event.setRobot(robot);
            listener.robotIsSkipStep(event);
        }
    }

    private void fireRobotIsExit(@NotNull Robot robot) {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            event.setRobot(robot);
            listener.robotIsExit(event);
        }
    }
}
