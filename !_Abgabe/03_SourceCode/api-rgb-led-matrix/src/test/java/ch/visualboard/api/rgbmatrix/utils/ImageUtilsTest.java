package ch.visualboard.api.rgbmatrix.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

@ExtendWith(MockitoExtension.class)
class ImageUtilsTest extends ImageUtilsTestObjects
{
    private BufferedImage bufferedImage;

    @BeforeEach
    void setUp() throws IOException
    {
        bufferedImage = ImageIO.read(ResourceUtils.getFile("classpath:test-image.png"));
    }

    @Test
    void testIf_convertImageToRgbMatrixBytes_isSuccessful()
    {
        String result = Arrays.toString(ImageUtils.convertImageToRgbMatrixBytes(bufferedImage));

        Assertions.assertEquals(Arrays.toString(EXPECTED_CONVERT_IMAGE_TO_RGB_MATRIX_BYTES), result);
    }

    @Test
    void testIf_makeSnakeline_isSuccessful()
    {
    }

    @Test
    void testIf_flipImage_isSuccessful()
    {
    }

    @Test
    void testIf_getBufferedImageFromFile_isSuccessful()
    {
    }

    @Test
    void testIf_makeTransparentImage_isSuccessful()
    {
    }

    @Test
    void testIf_rotateImage_isSuccessful()
    {
    }

    @Test
    void testIf_changeOpacity_isSuccessful()
    {
    }

    @Test
    void testIf_scaleImage_isSuccessful()
    {
    }
}
