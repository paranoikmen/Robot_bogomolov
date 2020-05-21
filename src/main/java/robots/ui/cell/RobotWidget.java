package robots.ui.cell;

import org.jetbrains.annotations.NotNull;
import robots.Direction;
import robots.IRobot;
import robots.PlayerRobot;
import robots.Robot;
import robots.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RobotWidget extends CellItemWidget {

    private final Robot robot;
    private final Color color;

    private IRobot iRobot;
    private PlayerRobot playerRobot;

    public RobotWidget(Robot robot, Color color) {
        super();
        this.robot = robot;
        this.color = color;
        setFocusable(true);
        addKeyListener(new KeyController());
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getRobotFileByColorAndActive(color, robot.isActive()));
            image = ImageUtils.resizeImage(image, 60, 96);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    CellWidget.Layer getLayer() {
        return CellWidget.Layer.BOTTOM;
    }

    public void setActive(boolean state) {
        setFocusable(state);
        requestFocus();
        repaint();
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(60, 120);
    }

    public Color getColor() {
        return color;
    }


    private static File getRobotFileByColorAndActive(Color robotColor, boolean active) {
        File file = null;
        if (robotColor == Color.RED)  {
            file = active ? new File("RRBA.png") : new File("RRB.png");
        }
        if (robotColor == Color.BLUE) {
            file = active ? new File("RBBA.png") : new File("RBB.png");
        }
        return file;
    }

    // Внутренний класс-обработчик событий. Придает специфицеское поведение виджету
    private class KeyController implements KeyListener {

        @Override
        public void keyTyped(KeyEvent arg0) {
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            int keyCode = ke.getKeyCode();

            moveAction(keyCode);
            skipStepAction(keyCode);

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        private void skipStepAction(@NotNull int keyCode) {
            if(keyCode == KeyEvent.VK_F) {
                robot.skipStep();
            }
        }

        private void moveAction(@NotNull int keyCode){
            Direction direction = directionByKeyCode(keyCode);
            System.out.println(color + " go to " + direction);

            if(direction != null && robot.isActive()) {
                if(!robot.getII()){
                    playerRobot = (PlayerRobot) robot;
                    robot.move(direction);
                }
                if(robot.getII()) {
                    iRobot = (IRobot) robot;
                    iRobot.move();

                }
            }
        }

        private Direction directionByKeyCode(@NotNull int keyCode) {
            Direction direction = null;
            switch (keyCode) {
                case KeyEvent.VK_W:
                    direction = Direction.NORTH;
                    break;
                case KeyEvent.VK_S:
                    direction = Direction.SOUTH;
                    break;
                case KeyEvent.VK_A:
                    direction = Direction.WEST;
                    break;
                case KeyEvent.VK_D:
                    direction = Direction.EAST;
                    break;
            }
            return direction;
        }
    }
}
