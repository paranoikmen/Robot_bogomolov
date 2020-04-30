package Mygame;

import Mygame.labirints.SmallLabirint;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Game game = new Game(new SmallLabirint());
        game.activeRobot().move(Direction.NORTH);
        game.activeRobot().move(Direction.NORTH);
        game.activeRobot().move(Direction.NORTH);
        game.activeRobot().move(Direction.NORTH);
        game.activeRobot().move(Direction.NORTH);
        game.activeRobot().move(Direction.NORTH);
    }
}
