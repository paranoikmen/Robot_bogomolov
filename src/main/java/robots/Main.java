package robots;

import org.jetbrains.annotations.NotNull;
import robots.event.GameActionEvent;
import robots.event.GameActionListener;
import robots.labirints.SmallLabirint;
import robots.ui.FieldWidget;
import robots.ui.WidgetFactory;
import robots.ui.utils.GameUtils;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SwingUtilities.invokeLater(GamePanel::new);
    }

    static class GamePanel extends JFrame {

        private Game game;
        private WidgetFactory widgetFactory;
        private PathFinder pathFinder = new PathFinder();

        public GamePanel() throws HeadlessException {
            setVisible(true);

            widgetFactory = new WidgetFactory();
            game = new Game(new SmallLabirint());

            game.addGameActionListener(new GameController());

            JPanel content = (JPanel) this.getContentPane();
            content.add(new FieldWidget(game.getGameField(), widgetFactory));

            widgetFactory.getWidget(game.getActiveRobot()).requestFocus();

            pack();
            setResizable(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            pathFinder.setGame(game);
        }

        private final class GameController implements GameActionListener {

            @Override
            public void robotIsMoved(@NotNull GameActionEvent event) {

            }

            @Override
            public void robotIsSkipStep(@NotNull GameActionEvent event) {

            }

            @Override
            public void robotIsExit(@NotNull GameActionEvent event) {

            }

            @Override
            public void gameStatusChanged(@NotNull GameActionEvent event) {
                GameStatus status = event.getStatus();
                switch (status) {
                    case WINNER_FOUND:
                        JOptionPane.showMessageDialog(GamePanel.this, "Выйграл робот: " +
                                GameUtils.colorName(widgetFactory.getWidget(game.getWinner()).getColor()));
                        break;
                    case GAME_ABORTED:
                        JOptionPane.showMessageDialog(GamePanel.this, "Игра завершена досрочно");
                        break;

                }
            }
        }
    }
}
