package Mygame.ui.block;

import Mygame.Orientation;
import Mygame.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WallWidget extends BlockWidget {

    public WallWidget(Orientation orientation) {
        super(orientation);
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getImageFileByOrientation());
            Dimension dimension = getDimensionByOrientation();
            image = ImageUtils.resizeImage(image, dimension.width, dimension.height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private File getImageFileByOrientation() {
        return (orientation == Orientation.VERTICAL) ? new File("WV.png") : new File("WH.png");
    }
}
