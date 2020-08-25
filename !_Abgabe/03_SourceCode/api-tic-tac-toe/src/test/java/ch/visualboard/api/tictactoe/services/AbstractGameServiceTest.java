package ch.visualboard.api.tictactoe.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractGameServiceTest
{
    private TestAbstractGameService testGameService;

    @BeforeEach
    void setUp()
    {
        testGameService = new TestAbstractGameService();
    }

    @Test
    void isValidGameRequestFalse()
    {
        OngoingGameDTO testOngoingGameDTO = new OngoingGameDTO();
        testOngoingGameDTO.setServerResponse(false);

        assertFalse(testGameService.isValidGameRequest(testOngoingGameDTO));
        assertFalse(testGameService.isValidGameRequest(new OngoingGameDTO()));
    }

    @Test
    void isValidGameRequestTrue()
    {
        OngoingGameDTO testOngoingGameDTO = new OngoingGameDTO();
        testOngoingGameDTO.setServerResponse(true);

        assertTrue(testGameService.isValidGameRequest(testOngoingGameDTO));
    }
}

class TestAbstractGameService extends AbstractGameService
{
}

