package CasioEDicPicConverter.wwwer;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConvertOnePic {
    static final BigDecimal HEIGHT = java.math.BigDecimal.valueOf(480);
    static final BigDecimal WIDTH = BigDecimal.valueOf(864);

    protected static void ConvertOnePic(File src , File dest) throws IOException {
            BufferedImage source = ImageIO.read(src);
            int[] arr = step(source,  WIDTH, HEIGHT);
            int targetWidth = arr[0];
            int targetHeight = arr[1];

            BufferedImage target = new BufferedImage(WIDTH.intValue(), HEIGHT.intValue(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = target.createGraphics();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, WIDTH.intValue(), HEIGHT.intValue());

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (WIDTH.intValue() - targetWidth) / 2;
            int y = (HEIGHT.intValue() - targetHeight) / 2;

            g2d.drawImage(source, x, y, targetWidth, targetHeight, null);
            g2d.dispose();
            ImageIO.write(target, "jpg", dest);

    }
    //计算尺寸
    protected static int[] step(BufferedImage img, BigDecimal WIDTH, BigDecimal HEIGHT) {
        BigDecimal sWidth = BigDecimal.valueOf(img.getWidth());
        BigDecimal sHeight = BigDecimal.valueOf(img.getHeight());
        BigDecimal imgRatio = sWidth.divide(sHeight, 10, RoundingMode.HALF_UP);
        BigDecimal targetRatio = WIDTH.divide(HEIGHT, 10, RoundingMode.HALF_UP);
        BigDecimal tWidth,tHeight;
        if (imgRatio.compareTo(targetRatio) >= 0) {
            tWidth = WIDTH;
            tHeight = WIDTH.divide(imgRatio, 10, RoundingMode.HALF_UP);
        } else {
            tHeight = HEIGHT;
            tWidth = HEIGHT.multiply(imgRatio);
        }
        if (tWidth.compareTo(WIDTH) > 0 || tHeight.compareTo(HEIGHT) > 0) {
            BigDecimal scale = WIDTH.divide(sWidth, 10, RoundingMode.HALF_UP)
                    .min(HEIGHT.divide(sHeight, 10, RoundingMode.HALF_UP));
            tWidth = sWidth.multiply(scale);
            tHeight = sHeight.multiply(scale);
        }
        return new int[]{tWidth.intValue(), tHeight.intValue()};
    }

    public static void ConvertOnePic(File src )throws IOException{
        File target = new File(src.getAbsolutePath().replace(getExtension(src), "_casio_ver."+"jpg"));
        ConvertOnePic(src,target);
    }

    public static String getExtension(File src) {
        return FilenameUtils.getExtension(src.getName());
    }
}

