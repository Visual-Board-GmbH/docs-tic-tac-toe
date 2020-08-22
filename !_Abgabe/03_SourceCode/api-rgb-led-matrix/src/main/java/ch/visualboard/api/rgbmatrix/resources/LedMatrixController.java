package ch.visualboard.api.rgbmatrix.resources;

import ch.visualboard.api.rgbmatrix.dtos.ImageUrlDTO;
import ch.visualboard.api.rgbmatrix.services.LedMatrixService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "rgb matrix")
@RestController("3dbc1a45-0fd6-4ec6-80eb-0d553101cef6")
@RequestMapping("/v1")
@Validated
public class LedMatrixController
{
    private final LedMatrixService ledMatrixService;

    @Autowired
    public LedMatrixController(final LedMatrixService ledMatrixService)
    {
        this.ledMatrixService = ledMatrixService;
    }

    @ApiOperation(
        value = "upload",
        notes = "Upload an image and shows it on rgb panel"
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "OK"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @RequestMapping(value = "/image-upload", method = RequestMethod.POST)
    public ResponseEntity<Object> imageUploadByFile(
        @RequestPart("image") MultipartFile image,
        @RequestParam(name = "rotationAngle", required = false, defaultValue = "0") @Min(0) @Max(360) int rotationAngle,
        @RequestParam(name = "matrixWidth", required = false, defaultValue = "16") @Min(0) int matrixWidth,
        @RequestParam(name = "matrixHeight", required = false, defaultValue = "16") @Min(0) int matrixHeight,
        @RequestParam(name = "doStretch", required = false, defaultValue = "false") boolean doStretch
    )
    {
        return ledMatrixService.showImageOnMatrix(
            image,
            rotationAngle,
            matrixWidth,
            matrixHeight,
            doStretch
        );
    }

    @ApiOperation(
        value = "upload",
        notes = "Upload an image and shows it on rgb panel without response - api-tic-tac-toe"
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "OK"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @RequestMapping(value = "/image-upload-void", method = RequestMethod.POST)
    public void imageUploadByFileReturnsVoid(
        @RequestPart("image") MultipartFile image,
        @RequestParam(name = "rotationAngle", required = false, defaultValue = "0") @Min(0) @Max(360) int rotationAngle,
        @RequestParam(name = "matrixWidth", required = false, defaultValue = "16") @Min(0) int matrixWidth,
        @RequestParam(name = "matrixHeight", required = false, defaultValue = "16") @Min(0) int matrixHeight,
        @RequestParam(name = "doStretch", required = false, defaultValue = "false") boolean doStretch
    )
    {
        ledMatrixService.showImageOnMatrix(
            image,
            rotationAngle,
            matrixWidth,
            matrixHeight,
            doStretch
        );
    }

    @ApiOperation(
        value = "upload",
        notes = "Upload an image and shows it on rgb panel"
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "OK"
        ),
        @ApiResponse(
            code = 500,
            message = "Internal Server Error"
        )
    })
    @RequestMapping(value = "/image-url", method = RequestMethod.POST)
    public ResponseEntity<Object> imageUploadByUrl(
        @RequestBody ImageUrlDTO imageUrlDTO,
        @RequestParam(name = "rotationAngle", required = false, defaultValue = "0") @Min(0) @Max(360) int rotationAngle,
        @RequestParam(name = "matrixWidth", required = false, defaultValue = "16") @Min(0) int matrixWidth,
        @RequestParam(name = "matrixHeight", required = false, defaultValue = "16") @Min(0) int matrixHeight,
        @RequestParam(name = "doStretch", required = false, defaultValue = "false") boolean doStretch
    )
    {
        return ledMatrixService.downloadAndShowImageOnMatrix(
            imageUrlDTO,
            rotationAngle,
            matrixHeight,
            matrixWidth,
            doStretch
        );
    }
}
