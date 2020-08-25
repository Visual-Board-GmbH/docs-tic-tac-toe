package ch.visualboard.api.tictactoe.resources;

import ch.visualboard.api.tictactoe.services.LEDRGBMatrixService;
import ch.visualboard.api.tictactoe.validations.ValidPlayerSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "LED RGB Matrix")
@RestController("2cb24bf9-1e55-45d6-b70a-be8f2cfaa189")
@RequestMapping(value = "/v1")
@Validated
public class LEDRGBMatrixController
{
    private final LEDRGBMatrixService ledrgbMatrixService;

    @Autowired
    public LEDRGBMatrixController(final LEDRGBMatrixService ledrgbMatrixService)
    {
        this.ledrgbMatrixService = ledrgbMatrixService;
    }

    @ApiOperation(
        value = "get",
        notes = "Get available LED RGB Matrix from database"
    )
    @GetMapping(
        value = "/matrix"
    )
    public ResponseEntity<Object> getLedRgbMatrixList(
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return ledrgbMatrixService.getLedRgbMatrixList();
    }
}
