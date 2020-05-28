package robots;

import org.jetbrains.annotations.NotNull;
import robots.event.*;
import robots.labirints.Labirint;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GameStatus gameStatus;
    private Robot activeRobot;
    private Robot winner;
    private Field gameField;
    private PathFinder pathFinder = new PathFinder();

    public Game(Labirint labirint) {
        initGame(labirint);
    }

    private void initGame(@NotNull Labirint labirint) {
        setStatus(GameStatus.GAME_IS_ON);

        buildField(labirint);

        gameField.addFieldlActionListener(new FieldObserver());

        for(var i : gameField.getRobotsOnField()) {
            i.addRobotActionListener(new RobotObserver());
        }

        // set active robot
        passMoveNextRobot();
    }

    public void abort() {
        setStatus(GameStatus.GAME_ABORTED);
        setActiveRobot(null);
    }

    public GameStatus getStatus() {
        return gameStatus;
    }

    private void setStatus(GameStatus status) {
        if(gameStatus != status) {
            gameStatus = status;
            fireGameStatusIsChanged(gameStatus);
        }
    }

    public Robot getWinner() {
        return winner;
    }

    public Robot getActiveRobot() {
        return activeRobot;
    }

    public Field getGameField() { return gameField; }


    private void passMoveNextRobot() {
        if(gameStatus != GameStatus.GAME_IS_ON) {
            setActiveRobot(null);
            return;
        }

        List<Robot> robotsOnField = gameField.getRobotsOnField();

        if(robotsOnField.size() == 1 ) {
            Robot robot = robotsOnField.get(0);
            setActiveRobot(robot);
        }
        else if (robotsOnField.size() == 2) {
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

        if(robotsOnField.size() == 2) {
            IRobot II = null;
            PlayerRobot playerRobot = null;

            if(robotsOnField.get(0).getII()) {
                II = (IRobot) robotsOnField.get(0);
                playerRobot = (PlayerRobot) robotsOnField.get(1);
            }
            else if(!robotsOnField.get(0).getII()) {
                II = (IRobot) robotsOnField.get(1);
                playerRobot = (PlayerRobot) robotsOnField.get(0);
            }

            if(playerRobot.getPosition().equals(gameField.getExitCell())) {
                setWinner(playerRobot);
                result = GameStatus.WIN_IS_PLAYER;
            }
        }
        else if(robotsOnField.size()==1) {
            if(robotsOnField.get(0).getPosition().equals(gameField.getExitCell())) {
                result = GameStatus.DRAW;
            }
            else {
                result = GameStatus.WIN_IS_II;
            }
        }

        return result;
    }



    private void buildField(@NotNull Labirint labirint) {
        gameField = labirint.buildField();
        pathFinder.setField(getGameField());
    }

    private void setWinner(@NotNull Robot robot) {
        winner = robot;
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
            GameStatus gameStatus = determineOutcomeGame();
            setStatus(gameStatus);
            if(gameStatus==GameStatus.GAME_IS_ON) {
                passMoveNextRobot();
            }

        }

        @Override
        public void robotSkippedStep(@NotNull RobotActionEvent event) {
            fireRobotIsSkipStep(event.getRobot());
            setStatus(determineOutcomeGame());
            passMoveNextRobot();
        }

        @Override
        public void robotActivityChanged(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }


    }

    private class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsExit(@NotNull FieldActionEvent event) {
            GameStatus status = determineOutcomeGame();
            setStatus(status);
            passMoveNextRobot();
            fireRobotIsTeleported(event.getRobot());
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

    private void fireRobotIsTeleported(@NotNull Robot robot) {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            event.setRobot(robot);
            listener.robotIsExit(event);
        }
    }

    private void fireGameStatusIsChanged(@NotNull GameStatus status) {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            event.setStatus(status);
            listener.gameStatusChanged(event);
        }
    }
}
