package Mygame;

import org.jetbrains.annotations.NotNull;
import Mygame.event.*;
import Mygame.labirints.Labirint;

import java.util.ArrayList;
import java.util.List;

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
