package ch.visualboard.api.tictactoe.tasks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.visualboard.api.tictactoe.clients.MQTTClient;
import ch.visualboard.api.tictactoe.dao.GameDAO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.mappers.OngoingGameMapper;
import ch.visualboard.api.tictactoe.repositories.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OngoingGameTaskTest
{
    @Mock
    private GameRepository mockGameRepository;

    @Mock
    private OngoingGameMapper mockOngoingGameMapper;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private MQTTClient mockMqttClient;

    @Mock
    private GameDAO mockGameDAO;

    @InjectMocks
    private OngoingGameTask ongoingGameTask;

    @Test
    void broadcastOngoingGames() throws JsonProcessingException
    {
        when(mockGameRepository.findAll()).thenReturn(Arrays.asList(mockGameDAO));
        when(mockOngoingGameMapper.mapGameDTOToOngoingGameDAO(any())).thenReturn(mock(OngoingGameDTO.class));
        when(mockObjectMapper.writeValueAsString(any())).thenReturn("json text");

        ongoingGameTask.broadcastOngoingGames();

        verify(mockGameRepository, times(1)).findAll();
        verify(mockOngoingGameMapper, times(1)).mapGameDTOToOngoingGameDAO(any());
        verify(mockMqttClient, times(1)).sendMessage("ttt/all_games", "json text");
    }
}
