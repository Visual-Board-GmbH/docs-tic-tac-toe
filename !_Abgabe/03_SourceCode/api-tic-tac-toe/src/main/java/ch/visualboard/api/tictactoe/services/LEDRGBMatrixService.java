package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dtos.LEDRGBMatrixDTO;
import ch.visualboard.api.tictactoe.dtos.MoveDTO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.dtos.PixelDTO;
import ch.visualboard.api.tictactoe.entities.GamePlayer;
import ch.visualboard.api.tictactoe.entities.TicTacToeGridPosition;
import ch.visualboard.api.tictactoe.entities.TicTacToeTile;
import ch.visualboard.api.tictactoe.repositories.LEDRGBMatrixRepository;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component("6063e136-96f1-4aa6-a1ad-14ad1a76faf2")
public class LEDRGBMatrixService
{
    private static final Logger logger = LoggerFactory.getLogger(LEDRGBMatrixService.class);

    private final LEDRGBMatrixRepository ledrgbMatrixRepository;

    @Autowired
    public LEDRGBMatrixService(final LEDRGBMatrixRepository ledrgbMatrixRepository)
    {
        this.ledrgbMatrixRepository = ledrgbMatrixRepository;
    }

    public BufferedImage assembleImage(OngoingGameDTO ongoingGameDTO)
    {
        try {
            BufferedImage assembledImage = ImageIO.read(ResourceUtils.getFile("classpath:images/grid.png"));

            for (MoveDTO moveDTO : ongoingGameDTO.getGameData().getMoves()) {
                drawImage(assembledImage, moveDTO.getPlayer(), moveDTO.getGridPosition());
            }

            return assembledImage;
        }
        catch (FileNotFoundException e) {
            logger.error("File not found.", e);
        }
        catch (IOException e) {
            logger.error("Something went wrong.", e);
        }

        return null;
    }

    public ResponseEntity<Object> getLedRgbMatrixList()
    {
        List<LEDRGBMatrixDTO> ledrgbMatrixDTOList = ledrgbMatrixRepository.findAll().stream()
            .map(matrixDAO -> LEDRGBMatrixDTO.builder().id(matrixDAO.getId()).available(matrixDAO.getAvailable()).build())
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(ledrgbMatrixDTOList).orElseGet(ArrayList::new));
    }

    private BufferedImage drawImage(BufferedImage draw, GamePlayer tile, TicTacToeGridPosition position) throws IOException
    {
        BufferedImage image = TicTacToeTile.getBufferedImageForTile(getTileForPlayer(tile));
        List<PixelDTO> pixelDTOList = getBufferedImageAsPixelDTOList(image);

        for (PixelDTO pixelDTO : pixelDTOList) {
            int xPosition = position.getXPositionStart() + pixelDTO.getXPosition();
            int yPosition = position.getYPositionStart() + pixelDTO.getYPosition();
            draw.setRGB(xPosition, yPosition, pixelDTO.getRgb());
        }

        return draw;
    }

    private TicTacToeTile getTileForPlayer(GamePlayer gamePlayer)
    {
        if (gamePlayer == GamePlayer.HOST) {
            return TicTacToeTile.X;
        }

        return TicTacToeTile.O;
    }

    private List<PixelDTO> getBufferedImageAsPixelDTOList(final BufferedImage image)
    {
        List<PixelDTO> pixelDTOList = new ArrayList<>();

        for (int iWidth = 0; iWidth < image.getWidth(); iWidth++) {
            for (int iHeight = 0; iHeight < image.getHeight(); iHeight++) {
                int rgb = image.getRGB(iWidth, iHeight);
                if (rgb != 0) {
                    pixelDTOList.add(new PixelDTO(iWidth, iHeight, rgb));
                }
            }
        }

        return pixelDTOList;
    }
}
