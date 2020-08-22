package ch.visualboard.api.rgbmatrix.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils
{
    public final static String LAST_IMAGE_FILENAME = "lastImage-1b00519d-1056-4f97-b8a5-4ca63358bbd2.png";

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException
    {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        return file;
    }

    public static void saveBufferedImageToFile(BufferedImage bufferedImage) throws IOException
    {
        ImageIO.write(bufferedImage, "png", new File(LAST_IMAGE_FILENAME));
    }
}
