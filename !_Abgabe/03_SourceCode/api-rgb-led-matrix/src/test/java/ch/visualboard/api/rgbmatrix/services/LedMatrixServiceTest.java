package ch.visualboard.api.rgbmatrix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import ch.visualboard.api.rgbmatrix.dtos.ImageUrlDTO;
import ch.visualboard.api.rgbmatrix.exceptions.SerialPortCouldNotBeOpenException;
import ch.visualboard.api.rgbmatrix.gateways.SerialPortGateway;
import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class LedMatrixServiceTest
{
    @Mock
    private SerialPortGateway mockSerialPortGateway;

    @InjectMocks
    private LedMatrixService ledMatrixService;

    @Test
    void testIf_showImageOnMatrix_isSuccessful() throws IOException, SerialPortCouldNotBeOpenException
    {
        File file = ResourceUtils.getFile("classpath:test-image.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));

        ResponseEntity<Object> responseEntity = ledMatrixService.showImageOnMatrix(
            multipartFile,
            0,
            16,
            16,
            false
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(mockSerialPortGateway).writeByteArrayToRgbMatrix(any());
    }

    @Test
    void testIf_downloadAndShowImageOnMatrix_isSuccessful() throws IOException, SerialPortCouldNotBeOpenException
    {
        ImageUrlDTO imageUrlDTO = new ImageUrlDTO();
        String filePath = ResourceUtils.getFile(this.getClass().getResource("/test-image.png")).toURI().toString();
        imageUrlDTO.setUrl(filePath);

        ResponseEntity<Object> responseEntity = ledMatrixService.downloadAndShowImageOnMatrix(
            imageUrlDTO,
            0,
            16,
            16,
            false
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(mockSerialPortGateway).writeByteArrayToRgbMatrix(any());
    }
}
