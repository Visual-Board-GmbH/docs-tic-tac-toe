package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dao.GameDAO;
import ch.visualboard.api.tictactoe.dao.GameHistoryDAO;
import ch.visualboard.api.tictactoe.dao.LEDRGBMatrixDAO;
import ch.visualboard.api.tictactoe.dao.PlayerDAO;
import ch.visualboard.api.tictactoe.dtos.GameDataDTO;
import ch.visualboard.api.tictactoe.dtos.MoveDTO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.entities.GameWinner;
import ch.visualboard.api.tictactoe.entities.State;
import ch.visualboard.api.tictactoe.entities.TicTacToeGridPosition;
import ch.visualboard.api.tictactoe.repositories.GameHistoryRepository;
import ch.visualboard.api.tictactoe.repositories.GameRepository;
import ch.visualboard.api.tictactoe.repositories.LEDRGBMatrixRepository;
import ch.visualboard.api.tictactoe.repositories.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("c0738360-db49-4552-90e6-4b0d8e4e4779")
public class TTTService
{
    private static final Logger logger = LoggerFactory.getLogger(TTTService.class);

    private final ObjectMapper objectMapper;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameHistoryRepository gameHistoryRepository;
    private final LEDRGBMatrixRepository ledrgbMatrixRepository;
    private final PlayerService playerService;

    @Autowired
    public TTTService(
        final ObjectMapper objectMapper,
        final GameRepository gameRepository,
        final PlayerRepository playerRepository,
        final GameHistoryRepository gameHistoryRepository,
        final LEDRGBMatrixRepository ledrgbMatrixRepository,
        final PlayerService playerService
    )
    {
        this.objectMapper = objectMapper;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.gameHistoryRepository = gameHistoryRepository;
        this.ledrgbMatrixRepository = ledrgbMatrixRepository;
        this.playerService = playerService;
    }

    public OngoingGameDTO processOngoingGameData(GameDAO gameDAO, OngoingGameDTO ongoingGameDTO) throws Exception
    {
        GameDataDTO ongoingGameDataDTO = ongoingGameDTO.getGameData();
        GameDataDTO gameDAOGameDataDTO = objectMapper.readValue(gameDAO.getGameData(), GameDataDTO.class);

        // handle specific states
        if (!ongoingGameDTO.getState().equals(State.ACTIVE)) {
            if (ongoingGameDTO.getState().equals(State.OPEN)) {
                if (gameDAOGameDataDTO.getGuest() > 0) {
                    gameDAO.getPlayerDAOS().removeIf(player -> player.getId() == gameDAOGameDataDTO.getGuest());
                    gameDAOGameDataDTO.setGuest(0);
                }
            }
            if (ongoingGameDTO.getState().equals(State.PENDING)) {
                if (ongoingGameDataDTO.getGuest() > 0 && gameDAOGameDataDTO.getGuest() == 0) {
                    Optional<PlayerDAO> playerDAOOptional = playerRepository.findById(ongoingGameDataDTO.getGuest());
                    if (!playerDAOOptional.isPresent()) {
                        throw new Exception();
                    }
                    gameDAO.getPlayerDAOS().add(playerDAOOptional.get());
                    gameDAOGameDataDTO.setGuest(ongoingGameDataDTO.getGuest());
                }
            }

            //matrix ids
            List<Integer> ongoingMatrixIds = ongoingGameDTO.getMatrixIds();
            List<Integer> alreadyOwnedMatrixIds = gameDAO.getLedrgbMatrixDAOS().stream().map(LEDRGBMatrixDAO::getId).collect(Collectors.toList());
            List<Integer> idsToAdd = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();

            if (ongoingMatrixIds != null && !ongoingMatrixIds.isEmpty()) {

                // collect ids to add
                for (Integer id : ongoingMatrixIds) {
                    if (alreadyOwnedMatrixIds.contains(id)) {
                        continue;
                    }
                    idsToAdd.add(id);
                }
                // check if ids available
                List<LEDRGBMatrixDAO> ledrgbMatrixDAOS = ledrgbMatrixRepository.findAllById(idsToAdd);
                for (LEDRGBMatrixDAO ledrgbMatrixDAO : ledrgbMatrixDAOS) {
                    if (!ledrgbMatrixDAO.getAvailable()) {
                        throw new Exception("Matrix with id: {" + ledrgbMatrixDAO.getId() + "} is not available");
                    }
                }
                //make added ledrgbMatrixs not available
                ledrgbMatrixDAOS.forEach(ledrgbMatrixDAO -> ledrgbMatrixDAO.setAvailable(false));
                ledrgbMatrixRepository.saveAll(ledrgbMatrixDAOS);
                // add ids to gameDAO
                gameDAO.getLedrgbMatrixDAOS().addAll(ledrgbMatrixDAOS);

                // collect ids to remove
                for (Integer id : alreadyOwnedMatrixIds) {
                    if (ongoingMatrixIds.contains(id)) {
                        continue;
                    }
                    idsToRemove.add(id);
                }
                // remove all idsToRemove from gameDAO
                gameDAO.getLedrgbMatrixDAOS()
                    .removeAll(gameDAO.getLedrgbMatrixDAOS()
                        .stream()
                        .filter(ledrgbMatrixDAO -> idsToRemove.contains(ledrgbMatrixDAO.getId()))
                        .collect(Collectors.toList()));

                //make removed ledrgbMatrixs available
                ledrgbMatrixDAOS = ledrgbMatrixRepository.findAllById(idsToRemove);
                ledrgbMatrixDAOS.forEach(ledrgbMatrixDAO -> ledrgbMatrixDAO.setAvailable(true));
                ledrgbMatrixRepository.saveAll(ledrgbMatrixDAOS);

                // set usingMatrix inside gameDAO
                gameDAO.setUsingMatrix(!gameDAO.getLedrgbMatrixDAOS().isEmpty());
            }
        }

        // if active game
        boolean bothPlayersSet = gameDAOGameDataDTO.getGuest() > 0 && gameDAOGameDataDTO.getHost() > 0;
        if (ongoingGameDTO.getState().equals(State.ACTIVE) && bothPlayersSet) {
            if (ongoingGameDataDTO.getMoves().size() != 0) {
                final List<MoveDTO> moves = gameDAOGameDataDTO.getMoves();
                MoveDTO currentMove = null;
                if (!moves.isEmpty()) {
                    currentMove = ongoingGameDataDTO.getMoves().get(ongoingGameDataDTO.getMoves().size() - 1);
                    if (!checkIfValidMove(moves, currentMove)) {
                        logger.error("Current move is not valid.");
                        throw new Exception("Current move is not valid.");
                    }
                }
                else {
                    currentMove = ongoingGameDataDTO.getMoves().get(0);
                }
                gameDAOGameDataDTO.getMoves().add(currentMove);
                if (checkForWinner(gameDAOGameDataDTO.getMoves())) {
                    gameDAOGameDataDTO.setWinner(GameWinner.valueOf(String.valueOf(currentMove.getPlayer())));
                }
                // check for draw
                if (gameDAOGameDataDTO.getMoves().size() == 9) {
                    gameDAOGameDataDTO.setWinner(GameWinner.NONE);
                }
            }
        }

        // prepare gameDAO
        gameDAO.setState(ongoingGameDTO.getState());
        gameDAO.setGameData(objectMapper.writeValueAsString(gameDAOGameDataDTO));
        gameDAO.setLastModified(new Date());

        // updating ongoingGameDTO
        ongoingGameDTO.setGameData(gameDAOGameDataDTO);
        ongoingGameDTO.setState(gameDAO.getState());
        ongoingGameDTO.setLastModified(objectMapper.writeValueAsString(gameDAO.getLastModified()));
        ongoingGameDTO.setMatrixIds(gameDAO.getLedrgbMatrixDAOS().stream().map(LEDRGBMatrixDAO::getId).collect(Collectors.toList()));
        List<PlayerDAO> playerDAOS = gameDAO.getPlayerDAOS();
        playerDAOS = playerDAOS.size() >= 3 ? orderPlayerDAO(gameDAO.getPlayerDAOS(), gameDAOGameDataDTO) : playerDAOS;
        gameDAO.setPlayerDAOS(playerDAOS);
        ongoingGameDTO.setPlayerData(playerService.getHostGuestPlayerData(playerDAOS));

        if (gameDAOGameDataDTO.getWinner() != null) {
            // convert gameDAO to gameHistory and add to players
            GameHistoryDAO gameHistoryDAO = GameHistoryDAO.builder()
                .gameId(gameDAO.getId())
                .gameName(gameDAO.getName())
                .gameData(objectMapper.writeValueAsString(gameDAOGameDataDTO))
                .playerData(objectMapper.writeValueAsString(ongoingGameDTO.getPlayerData()))
                .datePlayed(new Date())
                .playerDAOS(playerDAOS)
                .build();

            gameHistoryRepository.save(gameHistoryDAO);

            playerDAOS.forEach(playerDAO -> playerDAO.getGameHistoryDAOS().add(gameHistoryDAO));

            // save players
            playerRepository.saveAll(playerDAOS);
            gameRepository.delete(gameDAO);
        }
        else {
            // save to DB
            gameRepository.save(gameDAO);
        }

        return ongoingGameDTO;
    }

    private List<PlayerDAO> orderPlayerDAO(final List<PlayerDAO> playerDAOS, final GameDataDTO gameDAOGameDataDTO)
    {
        List<PlayerDAO> orderedPlayerDAOS = new ArrayList<>();
        Optional<PlayerDAO> optionalHost = playerDAOS.stream().filter(playerDAO -> playerDAO.getId() == gameDAOGameDataDTO.getHost()).findAny();
        Optional<PlayerDAO> optionalGuest = playerDAOS.stream().filter(playerDAO -> playerDAO.getId() == gameDAOGameDataDTO.getGuest()).findAny();

        if (optionalHost.isPresent()) {
            orderedPlayerDAOS.add(optionalHost.get());
        }

        if (optionalGuest.isPresent()) {
            orderedPlayerDAOS.add(optionalGuest.get());
        }

        return orderedPlayerDAOS;
    }

    private boolean checkForWinner(final List<MoveDTO> moves)
    {
        //https://stackoverflow.com/questions/1056316/algorithm-for-determining-tic-tac-toe-game-over
        String[][] board = new String[3][3];
        MoveDTO currentMove = moves.get(moves.size() - 1);
        int currentXPosition = currentMove.getGridPosition().getXPositionStart() / 6;
        int currentYPosition = currentMove.getGridPosition().getYPositionStart() / 6;
        String currentValue = currentMove.getPlayer().toString();

        // build empty board
        TicTacToeGridPosition enumValue = TicTacToeGridPosition.TOP_LEFT;
        for (TicTacToeGridPosition position : enumValue.getDeclaringClass().getEnumConstants()) {
            board[position.getXPositionStart() / 6][position.getYPositionStart() / 6] = "NONE";
        }

        // occupie board with current moves
        for (MoveDTO move : moves) {
            TicTacToeGridPosition gridPosition = move.getGridPosition();
            board[gridPosition.getXPositionStart() / 6][gridPosition.getYPositionStart() / 6] = String.valueOf(move.getPlayer());
        }

        //check col
        for (int i = 0; i < 3; i++) {
            if (!board[currentXPosition][i].equals(currentValue)) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }

        //check row
        for (int i = 0; i < 3; i++) {
            if (!board[i][currentYPosition].equals(currentValue)) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }

        if (currentXPosition == currentYPosition) {
            // diagonal
            for (int i = 0; i < 3; i++) {
                if (!board[i][i].equals(currentValue)) {
                    break;
                }
                if (i == 2) {
                    return true;
                }
            }
        }

        //check anti diagonal
        if (currentXPosition + currentYPosition == 2) {
            for (int i = 0; i < 3; i++) {
                if (!board[i][(2) - i].equals(currentValue)) {
                    break;
                }
                if (i == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkIfValidMove(List<MoveDTO> moves, MoveDTO currentMove)
    {
        MoveDTO lastMove = moves.get(moves.size() - 1);
        if (lastMove.getCount() + 1 != currentMove.getCount()) {
            return false;
        }
        if (lastMove.getPlayer() == currentMove.getPlayer()) {
            return false;
        }

        for (MoveDTO move : moves) {
            if (move.getGridPosition() != currentMove.getGridPosition()) {
                continue;
            }
            return false;
        }

        return true;
    }
}
