package ch.visualboard.api.rgbmatrix.test;

import ch.visualboard.api.rgbmatrix.ApiRgbMatrixApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApiRgbMatrixApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class RestAssuredTest
{
    @LocalServerPort
    protected int port;

    @BeforeEach
    public void setupBaseUrl()
    {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
}

