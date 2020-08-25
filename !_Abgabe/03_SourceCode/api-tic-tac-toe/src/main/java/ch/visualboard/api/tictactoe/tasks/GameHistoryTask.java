package ch.visualboard.api.tictactoe.tasks;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import ch.visualboard.api.tictactoe.dtos.GameHistoryDTO;
import ch.visualboard.api.tictactoe.mappers.GameHistoryMapper;
import ch.visualboard.api.tictactoe.repositories.GameHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("e4811d51-3dae-471e-b9b8-c511eb8fe644")
public class GameHistoryTask
{
    private static final Logger logger = LoggerFactory.getLogger(GameHistoryTask.class);

    private final GameHistoryRepository gameHistoryRepository;
    private final GameHistoryMapper gameHistoryMapper;
    private final MQTTClient mqttClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public GameHistoryTask(
        final GameHistoryRepository gameHistoryRepository,
        final GameHistoryMapper gameHistoryMapper,
        final MQTTClient mqttClient,
        final ObjectMapper objectMapper
    )
    {
        this.gameHistoryRepository = gameHistoryRepository;
        this.gameHistoryMapper = gameHistoryMapper;
        this.mqttClient = mqttClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1000)
    public void broadcastGameHistories() throws JsonProcessingException
    {
        List<GameHistoryDTO> gameHistoryDTOS = gameHistoryRepository.findAll()
            .stream()
            .map(gameHistoryMapper::mapGameHistoryDAOToGameHistoryDTO)
            .collect(Collectors.toList());

        mqttClient.sendMessage("ttt/all_game_histories", objectMapper.writeValueAsString(gameHistoryDTOS));
    }
}
