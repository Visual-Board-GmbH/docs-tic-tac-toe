package ch.visualboard.api.rgbmatrix.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ch.visualboard.api.rgbmatrix.dtos.ImageUrlDTO;
import ch.visualboard.api.rgbmatrix.services.LedMatrixService;
import ch.visualboard.api.rgbmatrix.test.RestAssuredTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;

class LedMatrixControllerTest extends RestAssuredTest
{
    @MockBean
    private LedMatrixService mockLedMatrixService;

    @Test
    void testIf_testimageUploadByFile_isSuccessful() throws FileNotFoundException
    {
        RestAssured
            .given()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("image", ResourceUtils.getFile("classpath:test-image.png"))
            .when()
            .post("/v1/image-upload")
            .then()
            .statusCode(HttpStatus.OK.value());

        verify(mockLedMatrixService).showImageOnMatrix(any(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForRequestParamValidationImageUploadByFile")
    void testIf_testimageUploadByFile_withInvalidRequestParamsReturnsHttpStatusBadRequest(
        int rotationAngle,
        int matrixWidth,
        int matrixHeight,
        boolean doStretch
    ) throws FileNotFoundException
    {
        RestAssured
            .given()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("image", ResourceUtils.getFile("classpath:test-image.png"))
            .when()
            .post(
                "/v1/image-upload?rotationAngle={rotationAngle}&matrixWidth={matrixWidth}&matrixHeight={matrixHeight}&doStretch={doStretch}",
                rotationAngle,
                matrixWidth,
                matrixHeight,
                doStretch
            )
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(mockLedMatrixService, never()).showImageOnMatrix(any(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @Test
    void testIf_imageUploadByUrl_isSuccessful() throws FileNotFoundException
    {
        ImageUrlDTO imageUrlDTO = new ImageUrlDTO();
        imageUrlDTO.setUrl(ResourceUtils.getFile("classpath:test-image.png").getAbsolutePath());
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(imageUrlDTO)
            .when()
            .post("/v1/image-url")
            .then()
            .statusCode(HttpStatus.OK.value());

        verify(mockLedMatrixService).downloadAndShowImageOnMatrix(any(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForRequestParamValidationImageUploadByFile")
    void testIf_imageUploadByUrl_withInvalidRequestParamsReturnsHttpStatusBadRequest(
        int rotationAngle,
        int matrixWidth,
        int matrixHeight,
        boolean doStretch
    ) throws FileNotFoundException
    {
        ImageUrlDTO imageUrlDTO = new ImageUrlDTO();
        imageUrlDTO.setUrl(ResourceUtils.getFile("classpath:test-image.png").getAbsolutePath());
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(imageUrlDTO)
            .when()
            .post(
                "/v1/image-url?rotationAngle={rotationAngle}&matrixWidth={matrixWidth}&matrixHeight={matrixHeight}&doStretch={doStretch}",
                rotationAngle,
                matrixWidth,
                matrixHeight,
                doStretch
            )
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(mockLedMatrixService, never()).downloadAndShowImageOnMatrix(any(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    private static Object[][] provideTestDataForRequestParamValidationImageUploadByFile()
    {
        return new Object[][]{
            {"-1", "16", "16", false},
            {"361", "16", "16", false},
            {"0", "-1", "16", false},
            {"0", "16", "-1", false}
        };
    }
}
