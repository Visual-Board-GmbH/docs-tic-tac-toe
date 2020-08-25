package ch.visualboard.api.tictactoe.tasks;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.mappers.OngoingGameMapper;
import ch.visualboard.api.tictactoe.repositories.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("fef918e2-0820-4a60-9242-9f14881d5f0e")
public class OngoingGameTask
{
    private static final Logger logger = LoggerFactory.getLogger(OngoingGameTask.class);

    private final GameRepository gameRepository;
    private final OngoingGameMapper ongoingGameMapper;
    private final ObjectMapper objectMapper;
    private final MQTTClient mqttClient;

    @Autowired
    public OngoingGameTask(
        final GameRepository gameRepository,
        final OngoingGameMapper ongoingGameMapper,
        final MQTTClient mqttClient,
        final ObjectMapper objectMapper
    )
    {
        this.mqttClient = mqttClient;
        this.gameRepository = gameRepository;
        this.ongoingGameMapper = ongoingGameMapper;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1000)
    public void broadcastOngoingGames() throws JsonProcessingException
    {
        List<OngoingGameDTO> ongoingGameDTOS;
        ongoingGameDTOS = gameRepository.findAll()
            .stream()
            .map(ongoingGameMapper::mapGameDTOToOngoingGameDAO)
            .collect(Collectors.toList());

        mqttClient.sendMessage("ttt/all_games", objectMapper.writeValueAsString(ongoingGameDTOS));
    }
}
