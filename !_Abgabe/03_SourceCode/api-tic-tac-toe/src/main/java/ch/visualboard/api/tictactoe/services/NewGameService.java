package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dao.GameDAO;
import ch.visualboard.api.tictactoe.dao.PlayerDAO;
import ch.visualboard.api.tictactoe.dtos.GameDataDTO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.entities.State;
import ch.visualboard.api.tictactoe.repositories.GameRepository;
import ch.visualboard.api.tictactoe.repositories.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("6513e011-b981-4c06-b60e-2564215bb9f2")
public class NewGameService extends AbstractGameService
{
    private static final Logger logger = LoggerFactory.getLogger(NewGameService.class);

    private final ObjectMapper objectMapper;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final MqttMessageService mqttMessageService;
    private final PlayerService playerService;

    @Autowired
    public NewGameService(
        final ObjectMapper objectMapper,
        final PlayerRepository playerRepository,
        final GameRepository gameRepository,
        final MqttMessageService mqttMessageService,
        final PlayerService playerService
    )
    {
        this.objectMapper = objectMapper;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.mqttMessageService = mqttMessageService;
        this.playerService = playerService;
    }

    public void handleGame(String topic, String message)
    {
        OngoingGameDTO ongoingGameDTO = null;
        try {
            ongoingGameDTO = objectMapper.readValue(message, OngoingGameDTO.class);

            if (isValidGameRequest(ongoingGameDTO) || ongoingGameDTO.getGameId() != null) {
                return;
            }

            GameDataDTO ongoingGameDataDTO = ongoingGameDTO.getGameData();
            ongoingGameDataDTO.setMoves(new ArrayList<>());
            List<Integer> playerIds = new ArrayList<>();
            playerIds.add(ongoingGameDataDTO.getHost());
            List<PlayerDAO> playerDAOs = playerRepository.findAllById(playerIds);

            GameDAO gameDAO = GameDAO.builder()
                .name(ongoingGameDTO.getName())
                .state(State.OPEN)
                .usingMatrix(false)
                .gameData(objectMapper.writeValueAsString(ongoingGameDataDTO))
                .lastModified(new Date())
                .playerDAOS(playerDAOs)
                .build();

            gameDAO = gameRepository.save(gameDAO);

            ongoingGameDTO.setPlayerData(playerService.getHostGuestPlayerData(playerDAOs));
            ongoingGameDTO.setState(gameDAO.getState());
            ongoingGameDTO.setLastModified(gameDAO.getLastModified().toString());
            ongoingGameDTO.setGameId(gameDAO.getId());
            mqttMessageService.sendValidOngoingGameByTopic(ongoingGameDTO, topic);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            mqttMessageService.sendInvalidOngoingGameByTopic(ongoingGameDTO, topic);
        }
    }
}
