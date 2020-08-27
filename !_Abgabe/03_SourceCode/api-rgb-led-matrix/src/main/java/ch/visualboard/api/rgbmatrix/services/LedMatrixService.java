package ch.visualboard.api.rgbmatrix.services;

import ch.visualboard.api.rgbmatrix.dtos.ApiErrorDTO;
import ch.visualboard.api.rgbmatrix.dtos.ImageUrlDTO;
import ch.visualboard.api.rgbmatrix.gateways.SerialPortGateway;
import ch.visualboard.api.rgbmatrix.utils.FileUtils;
import ch.visualboard.api.rgbmatrix.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("7e39776b-d0e7-4534-98ee-cd6333e94128")
public class LedMatrixService
{
    private static Logger LOGGER = LoggerFactory.getLogger(LedMatrixService.class);

    private final SerialPortGateway serialPortGateway;

    @Autowired
    public LedMatrixService(final SerialPortGateway serialPortGateway)
    {
        this.serialPortGateway = serialPortGateway;
    }

    public ResponseEntity<Object> showImageOnMatrix(
        final MultipartFile multipartFile,
        final int rotationAngle,
        final int matrixWidth,
        final int matrixHeight,
        final boolean doStretch
    )
    {
        try {
            File file = FileUtils.convertMultipartFileToFile(multipartFile);

            BufferedImage bufferedImage = ImageUtils.getBufferedImageFromFile(file);

            byte[] imageByteArray = doImageTransformationsAndSaveLastImage(rotationAngle, matrixWidth, matrixHeight, doStretch, bufferedImage);

            serialPortGateway.writeByteArrayToRgbMatrix(imageByteArray);
        }
        catch (IOException e) {
            String message = "File could not be processed - " + e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            LOGGER.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        catch (Exception e) {
            String message = e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            LOGGER.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Object> downloadAndShowImageOnMatrix(
        final ImageUrlDTO imageUrlDTO,
        final int rotationAngle,
        final int matrixWidth,
        final int matrixHeight,
        final boolean doStretch
    )
    {
        try {
            URL url = new URL(imageUrlDTO.getUrl());
            BufferedImage bufferedImage = ImageIO.read(url);

            byte[] imageByteArray = doImageTransformationsAndSaveLastImage(rotationAngle, matrixWidth, matrixHeight, doStretch, bufferedImage);

            serialPortGateway.writeByteArrayToRgbMatrix(imageByteArray);
        }
        catch (IOException e) {
            String message = "File could not be processed - " + e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            LOGGER.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        catch (Exception e) {
            String message = e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            LOGGER.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public void showLastDisplayedImage()
    {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(FileUtils.LAST_IMAGE_FILENAME));

            byte[] imageByteArray = ImageUtils.convertImageToRgbMatrixBytes(bufferedImage);
            serialPortGateway.writeByteArrayToRgbMatrix(imageByteArray);
        }
        catch (IOException e) {
            LOGGER.error("File could not be processed - " + e.getMessage());

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private byte[] doImageTransformationsAndSaveLastImage(
        final int rotationAngle,
        final int matrixWidth,
        final int matrixHeight,
        final boolean doStretch,
        BufferedImage bufferedImage
    ) throws IOException
    {
        if (bufferedImage.getWidth() != matrixWidth || bufferedImage.getWidth() != matrixHeight) {
            if (!doStretch) {
                bufferedImage = ImageUtils.fitImage(bufferedImage);
            }
            bufferedImage = ImageUtils.scaleImage(bufferedImage, matrixWidth, matrixHeight);
        }

        bufferedImage = ImageUtils.rotateImage(bufferedImage, rotationAngle);
        bufferedImage = ImageUtils.makeSnakeline(bufferedImage);

        FileUtils.saveBufferedImageToFile(bufferedImage);

        return ImageUtils.convertImageToRgbMatrixBytes(bufferedImage);
    }
}

