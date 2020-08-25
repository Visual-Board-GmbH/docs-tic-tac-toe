package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.clients.LEDRGBMatrixClient;
import ch.visualboard.api.tictactoe.dao.GameDAO;
import ch.visualboard.api.tictactoe.dao.LEDRGBMatrixDAO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.repositories.GameRepository;
import ch.visualboard.api.tictactoe.repositories.LEDRGBMatrixRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dab44156-2303-4973-8a76-bde2fda01b23")
public class OngoingGameService extends AbstractGameService
{
    private static final Logger logger = LoggerFactory.getLogger(OngoingGameService.class);

    private final LEDRGBMatrixClient ledrgbMatrixClient;
    private final LEDRGBMatrixService ledrgbMatrixService;
    private final LEDRGBMatrixRepository ledrgbMatrixRepository;
    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;
    private final TTTService tttService;
    private final MqttMessageService mqttMessageService;

    @Autowired
    public OngoingGameService(
        final LEDRGBMatrixClient ledrgbMatrixClient,
        final LEDRGBMatrixService ledrgbMatrixService,
        final LEDRGBMatrixRepository ledrgbMatrixRepository,
        final GameRepository gameRepository,
        final ObjectMapper objectMapper,
        final TTTService tttService,
        final MqttMessageService mqttMessageService
    )
    {
        this.ledrgbMatrixClient = ledrgbMatrixClient;
        this.ledrgbMatrixService = ledrgbMatrixService;
        this.ledrgbMatrixRepository = ledrgbMatrixRepository;
        this.gameRepository = gameRepository;
        this.objectMapper = objectMapper;
        this.tttService = tttService;
        this.mqttMessageService = mqttMessageService;
    }

    public void handleGame(String topic, String message)
    {
        OngoingGameDTO ongoingGameDTO = null;
        try {
            ongoingGameDTO = objectMapper.readValue(message, OngoingGameDTO.class);
            if (isValidGameRequest(ongoingGameDTO)) {
                return;
            }

            // check if ongoing game exists
            Optional<GameDAO> gameDAOOptional = gameRepository.findById(ongoingGameDTO.getGameId());
            if (!gameDAOOptional.isPresent()) {
                mqttMessageService.sendInvalidOngoingGameByTopic(ongoingGameDTO, topic);
            }
            GameDAO gameDAO = gameDAOOptional.get();

            //process ongoing game
            ongoingGameDTO = tttService.processOngoingGameData(gameDAO, ongoingGameDTO);
            mqttMessageService.sendValidOngoingGameByTopic(ongoingGameDTO, topic);

            // if matrix is used => build image and send to each matrix
            if (ongoingGameDTO.getMatrixIds().size() != 0) {
                List<LEDRGBMatrixDAO> ledrgbMatrixDAOS = gameDAO.getLedrgbMatrixDAOS();
                BufferedImage assembledImage = ledrgbMatrixService.assembleImage(ongoingGameDTO);
                for (LEDRGBMatrixDAO ledrgbMatrixDAO : ledrgbMatrixDAOS) {
                    ledrgbMatrixClient.sendImageToRgbMatrix(ledrgbMatrixDAO, assembledImage);
                }

                if (ongoingGameDTO.getGameData().getWinner() != null) {
                    // remove players and LEDRGBMatrixes from game
                    gameDAO.setPlayerDAOS(null);
                    gameDAO.setLedrgbMatrixDAOS(null);
                    // LEDRGBMatrix set available to true
                    ledrgbMatrixDAOS.forEach(ledrgbMatrixDAO -> ledrgbMatrixDAO.setAvailable(true));
                    ledrgbMatrixRepository.saveAll(ledrgbMatrixDAOS);
                    // delete gameDAO
                    gameRepository.delete(gameDAO);
                }
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            mqttMessageService.sendInvalidOngoingGameByTopic(ongoingGameDTO, topic);
        }
    }
}
