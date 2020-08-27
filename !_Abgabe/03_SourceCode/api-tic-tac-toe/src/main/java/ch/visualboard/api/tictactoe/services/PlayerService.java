package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dao.PlayerDAO;
import ch.visualboard.api.tictactoe.dtos.ApiErrorDTO;
import ch.visualboard.api.tictactoe.dtos.AuthenticatePlayerDTO;
import ch.visualboard.api.tictactoe.dtos.CreateOrUpdatePlayerDTO;
import ch.visualboard.api.tictactoe.dtos.LoggedInPlayerDTO;
import ch.visualboard.api.tictactoe.dtos.PlayerDataDTO;
import ch.visualboard.api.tictactoe.entities.GamePlayer;
import ch.visualboard.api.tictactoe.mappers.PlayerMapper;
import ch.visualboard.api.tictactoe.repositories.PlayerRepository;
import ch.visualboard.api.tictactoe.utils.CipherUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("f5036d92-4b99-4a35-a5c5-213049ae527f")
public class PlayerService
{
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(
        final PlayerMapper playerMapper,
        final PlayerRepository playerRepository
    )
    {
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
    }

    public ResponseEntity<Object> createNewPlayer(final CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO)
    {
        try {
            String salt = CipherUtils.generateRandomToken();
            String cryptedPassword = CipherUtils.cryptPasswordWithSHA256(createOrUpdatePlayerDTO.getPassword(), salt);

            playerRepository.save(playerMapper.mapCreateNewPlayerDTOToPlayerDAO(createOrUpdatePlayerDTO, cryptedPassword, salt));
        }
        catch (Exception e) {
            String message = e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getPlayerByNickname(final String nickname)
    {
        PlayerDAO playerDAO = playerRepository.findByNickname(nickname);

        if (playerDAO == null) {
            String message = "Player with nickname '" + nickname + "' not found.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.NOT_FOUND, message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        return ResponseEntity.ok().body(playerMapper.mapPlayerDAOToOtherPlayerDTO(playerDAO));
    }

    public ResponseEntity<Object> removePlayer(final Integer playerId, final @Valid HttpSession session)
    {
        try {
            LoggedInPlayerDTO player = (LoggedInPlayerDTO) session.getAttribute("player");
            if (player.getId() != playerId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        catch (Exception e) {
            String message = "Something went wrong.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        playerRepository.deleteById(playerId);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> updatePlayer(
        final CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO,
        final Integer playerId,
        final @Valid HttpSession session
    )
    {
        try {
            LoggedInPlayerDTO player = (LoggedInPlayerDTO) session.getAttribute("player");
            if (player.getId() != playerId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        catch (Exception e) {
            String message = "Something went wrong.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        PlayerDAO playerDAO = playerRepository.findById(playerId).orElse(null);
        if (playerDAO == null) {
            String message = "Player with ID '" + playerId + "' not found.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.NOT_FOUND, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        try {
            String salt = CipherUtils.generateRandomToken();
            String cryptedPassword = CipherUtils.cryptPasswordWithSHA256(createOrUpdatePlayerDTO.getPassword(), salt);
            PlayerDAO mappedPlayerDAO = playerMapper.mapUpdatedPlayerDTOToPlayerDAO(createOrUpdatePlayerDTO, playerId, cryptedPassword, salt);

            playerRepository.save(mappedPlayerDAO);
        }
        catch (Exception e) {
            String message = e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> uploadPlayerImager(final MultipartFile multipartFile, final Integer playerId, final HttpSession session)
    {
        try {
            LoggedInPlayerDTO player = (LoggedInPlayerDTO) session.getAttribute("player");
            if (player.getId() != playerId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        catch (Exception e) {
            String message = "Something went wrong.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        PlayerDAO playerDAO = playerRepository.findById(playerId).orElse(null);
        if (playerDAO == null) {
            String message = "Player with ID '" + playerId + "' not found.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.NOT_FOUND, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        try {
            if (!multipartFile.isEmpty()) {
                playerDAO.setTileImage(multipartFile.getBytes());
                playerRepository.save(playerDAO);
            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

            logger.error(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> authenticatePlayer(final AuthenticatePlayerDTO authenticatePlayerDTO, final HttpSession session)
    {
        PlayerDAO playerDAO = playerRepository.findByUsername(authenticatePlayerDTO.getUsername());

        if (playerDAO == null) {
            String message = "Player '" + authenticatePlayerDTO.getUsername() + "' not found.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.NOT_FOUND, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        if (isPasswordMatching(authenticatePlayerDTO.getPassword(), playerDAO.getPassword(), playerDAO.getSalt())) {
            LoggedInPlayerDTO playerDTO = playerMapper.mapPlayerDAOToLoggedInPlayerDTO(playerDAO);

            session.setAttribute("player", playerDTO);

            return ResponseEntity.status(HttpStatus.OK).body(playerDTO);
        }

        String message = "Wrong Password.";
        ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.UNAUTHORIZED, message);

        logger.info(message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public ResponseEntity<Object> getPlayerImager(final Integer playerId, final HttpSession session)
    {
        PlayerDAO playerDAO = playerRepository.findById(playerId).orElse(null);
        if (playerDAO == null) {
            String message = "Player with ID '" + playerId + "' not found.";
            ApiErrorDTO responseBody = new ApiErrorDTO(HttpStatus.NOT_FOUND, message);

            logger.info(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        if (playerDAO.getTileImage() == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(playerDAO.getTileImage());
    }

    private boolean isPasswordMatching(final String password, final String dbUserPassword, final String salt)
    {
        String cryptedPassword = CipherUtils.cryptPasswordWithSHA256(password, salt);
        return dbUserPassword.equals(cryptedPassword);
    }

    public ResponseEntity<Object> restrictPlayer(final HttpSession session)
    {
        session.removeAttribute("player");

        return ResponseEntity.ok().build();
    }

    public List<PlayerDataDTO> getHostGuestPlayerData(List<PlayerDAO> playerDAOList)
    {
        List<PlayerDataDTO> playerDataDTOList = new ArrayList<>();
        if (playerDAOList != null) {
            playerDataDTOList.add(createPlayerData(playerDAOList.get(0), GamePlayer.HOST));
        }

        if (playerDAOList.size() > 1) {
            playerDataDTOList.add(createPlayerData(playerDAOList.get(1), GamePlayer.GUEST));
        }

        return playerDataDTOList;
    }

    private PlayerDataDTO createPlayerData(PlayerDAO playerDAO, GamePlayer role)
    {
        return PlayerDataDTO.builder()
            .player(role)
            .displayName(playerDAO.getUsername())
            .build();
    }
}
