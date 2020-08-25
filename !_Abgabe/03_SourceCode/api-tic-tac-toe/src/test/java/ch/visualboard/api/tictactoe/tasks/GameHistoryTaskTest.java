package ch.visualboard.api.tictactoe.tasks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import ch.visualboard.api.tictactoe.dao.GameHistoryDAO;
import ch.visualboard.api.tictactoe.dtos.GameHistoryDTO;
import ch.visualboard.api.tictactoe.mappers.GameHistoryMapper;
import ch.visualboard.api.tictactoe.repositories.GameHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameHistoryTaskTest
{
    @Mock
    private GameHistoryRepository mockGameHistoryRepository;

    @Mock
    private GameHistoryMapper mockGameHistoryMapper;

    @Mock
    private MQTTClient mockMqttClient;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private GameHistoryDAO mockGameHistoryDAO;

    @InjectMocks
    private GameHistoryTask gameHistoryTask;

    @Test
    void broadcastGameHistories() throws JsonProcessingException
    {
        when(mockGameHistoryRepository.findAll()).thenReturn(Arrays.asList(mockGameHistoryDAO));
        when(mockGameHistoryMapper.mapGameHistoryDAOToGameHistoryDTO(any())).thenReturn(mock(GameHistoryDTO.class));
        when(mockObjectMapper.writeValueAsString(any())).thenReturn("json text");

        gameHistoryTask.broadcastGameHistories();

        verify(mockGameHistoryRepository, times(1)).findAll();
        verify(mockGameHistoryMapper, times(1)).mapGameHistoryDAOToGameHistoryDTO(any());
        verify(mockMqttClient, times(1)).sendMessage("ttt/all_game_histories", "json text");
    }
}

