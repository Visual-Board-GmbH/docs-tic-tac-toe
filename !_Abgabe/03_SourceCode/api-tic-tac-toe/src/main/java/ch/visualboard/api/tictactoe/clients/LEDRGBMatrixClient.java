package ch.visualboard.api.tictactoe.clients;

import ch.visualboard.api.tictactoe.dao.LEDRGBMatrixDAO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component("0204f010-1806-42a2-8928-6875a40323c5")
public class LEDRGBMatrixClient
{
    private final RestTemplate restTemplate;

    @Autowired
    public LEDRGBMatrixClient(
        final @Lazy RestTemplate restTemplate
    )
    {
        this.restTemplate = restTemplate;
    }

    public void sendImageToRgbMatrix(final LEDRGBMatrixDAO ledrgbMatrixDAO, BufferedImage bufferedImage) throws IOException
    {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", getFileFromBufferedImage(bufferedImage));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        restTemplate.postForEntity(
            ledrgbMatrixDAO.getUrl() + "/v1/image-upload-void",
            requestEntity,
            Void.class
        );
    }

    private Object getFileFromBufferedImage(BufferedImage bufferedImage) throws IOException
    {
        File image = new File("image.png");
        ImageIO.write(bufferedImage, "png", image);

        return new FileSystemResource(image);
    }
}
