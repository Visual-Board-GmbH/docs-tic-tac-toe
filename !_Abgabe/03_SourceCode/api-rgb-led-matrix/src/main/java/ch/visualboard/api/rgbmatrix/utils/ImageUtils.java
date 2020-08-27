package ch.visualboard.api.rgbmatrix.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;

public class ImageUtils
{
    public static byte[] convertImageToRgbMatrixBytes(BufferedImage bufferedImage)
    {
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        Color[][] colors = new Color[imageWidth][imageHeight];
        StringBuilder sb = new StringBuilder();

        int lineNumber = 1;

        for (int y = 0; y < imageHeight; y++) {
            String line = "";

            int halfLedLengthCounter = 0;

            for (int x = 0; x < imageWidth; x++) {
                colors[y][x] = new Color(bufferedImage.getRGB(x, y));

                if (halfLedLengthCounter != 0 && halfLedLengthCounter % 8 == 0) {
                    sb.append("<" + toFormatedHex(lineNumber++, false) + line + ">");
                    line = "";
                }

                line += buildRgbString(colors[y][x]);

                halfLedLengthCounter++;
            }

            sb.append("<" + toFormatedHex(lineNumber++, false) + line + ">");
        }

        return sb.toString().getBytes();
    }

    public static BufferedImage makeSnakeline(BufferedImage image)
    {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        for (int srcX = 0, trgX = imageWidth - 1; srcX < imageWidth; srcX++, trgX--) {
            for (int srcY = 0, trgY = 0; srcY < imageHeight; srcY++, trgY++) {

                int pixel = image.getRGB(srcX, srcY);
                if (srcY % 2 == 1) {
                    bufferedImage.setRGB(trgX, trgY, pixel);
                }
                else {
                    bufferedImage.setRGB(srcX, srcY, pixel);
                }
            }
        }

        return bufferedImage;
    }

    private static String buildRgbString(final Color color)
    {
        return toFormatedHex(color.getGreen(), true) + toFormatedHex(color.getRed(), true) + toFormatedHex(color.getBlue(), true);
    }

    private static String toFormatedHex(final int colorValue, boolean toUpperCase)
    {
        String value = StringUtils.leftPad(Integer.toHexString(colorValue), 2, '0').toLowerCase();
        return toUpperCase ? value.toUpperCase() : value;
    }

    public static BufferedImage getBufferedImageFromFile(File file) throws IOException
    {
        return ImageIO.read(file);
    }

    public static BufferedImage rotateImage(final BufferedImage bufferedImage, final int rotationAngle)
    {
        BufferedImage dest = new BufferedImage(bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getType());
        double theta = (Math.PI * 2) / 360 * rotationAngle;
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate(0, 0);
        graphics2D.rotate(theta, imageWidth / 2, imageHeight / 2);
        graphics2D.drawRenderedImage(bufferedImage, null);

        return dest;
    }

    // https://stackoverflow.com/a/35637914
    public static BufferedImage scaleImage(final BufferedImage bufferedImage, final int matrixWidth, final int matrixHeight)
    {
        BufferedImage scaledImage = new BufferedImage(matrixWidth, matrixHeight, bufferedImage.getType());

        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(bufferedImage, 0, 0, matrixWidth, matrixHeight, null);
        graphics2D.dispose();

        return scaledImage;
    }

    public static BufferedImage fitImage(final BufferedImage bufferedImage)
    {
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        if (imageWidth < imageHeight) {
            BufferedImage scaledImage = new BufferedImage(imageHeight, imageHeight, bufferedImage.getType());

            Graphics2D graphics2D = scaledImage.createGraphics();
            int blackBarSizeX = (imageHeight - imageWidth) / 2;
            graphics2D.drawImage(bufferedImage, blackBarSizeX, 0, null);
            graphics2D.dispose();

            return scaledImage;
        }

        if (imageWidth > imageHeight) {
            BufferedImage scaledImage = new BufferedImage(imageWidth, imageWidth, bufferedImage.getType());

            Graphics2D graphics2D = scaledImage.createGraphics();
            int blackBarSizeY = (imageWidth - imageHeight) / 2;
            graphics2D.drawImage(bufferedImage, 0, blackBarSizeY, null);
            graphics2D.dispose();

            return scaledImage;
        }

        return bufferedImage;
    }
}
