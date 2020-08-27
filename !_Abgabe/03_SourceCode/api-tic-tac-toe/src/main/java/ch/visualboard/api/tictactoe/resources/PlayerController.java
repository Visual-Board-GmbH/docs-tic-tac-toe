package ch.visualboard.api.tictactoe.resources;

import ch.visualboard.api.tictactoe.dtos.AuthenticatePlayerDTO;
import ch.visualboard.api.tictactoe.dtos.CreateOrUpdatePlayerDTO;
import ch.visualboard.api.tictactoe.services.PlayerService;
import ch.visualboard.api.tictactoe.validations.ValidPlayerSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Player")
@RestController("3067b579-f751-49a1-9ce1-790b68432b27")
@RequestMapping(value = "/v1")
@Validated
public class PlayerController
{
    private final PlayerService playerService;

    @Autowired
    public PlayerController(final PlayerService playerService)
    {
        this.playerService = playerService;
    }

    @ApiOperation(
        value = "create",
        notes = "Create new Player in the database"
    )
    @PostMapping(value = "/player")
    public ResponseEntity<Object> createPlayer(
        @RequestBody @Valid CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO
    )
    {
        return playerService.createNewPlayer(createOrUpdatePlayerDTO);
    }

    @ApiOperation(
        value = "get",
        notes = "Get Player from database"
    )
    @GetMapping(
        value = "/player/nickname/{nickname}"
    )
    public ResponseEntity<Object> getPlayerByNickname(
        @PathVariable final String nickname,
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return playerService.getPlayerByNickname(nickname);
    }

    @ApiOperation(
        value = "delete",
        notes = "Delete Player in database"
    )
    @DeleteMapping(
        value = "/player/{playerId}"
    )
    public ResponseEntity<Object> removePlayer(
        @PathVariable final Integer playerId,
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return playerService.removePlayer(playerId, session);
    }

    @ApiOperation(
        value = "update",
        notes = "Update Player in database"
    )
    @PutMapping(
        value = "/player/{playerId}"
    )
    public ResponseEntity<Object> updatePlayer(
        @PathVariable final Integer playerId,
        @RequestBody @Valid CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO,
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return playerService.updatePlayer(createOrUpdatePlayerDTO, playerId, session);
    }

    @ApiOperation(
        value = "upload player image",
        notes = "Update Player in image in database"
    )
    @PostMapping(
        value = "/player/{playerId}/image"
    )
    public ResponseEntity<Object> uploadPlayerImage(
        @RequestPart("image") MultipartFile image,
        @PathVariable final Integer playerId,
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return playerService.uploadPlayerImager(image, playerId, session);
    }

    @ApiOperation(
        value = "get player image",
        notes = "get Player image from database"
    )
    @GetMapping(
        value = "/player/{playerId}/image"
    )
    public ResponseEntity<Object> getPlayerImage(
        @PathVariable final Integer playerId,
        @ApiIgnore @ValidPlayerSession @Valid HttpSession session
    )
    {
        return playerService.getPlayerImager(playerId, session);
    }

    @ApiOperation(
        value = "login",
        notes = "authenticate player data"
    )
    @PostMapping(value = "/player/authenticate")
    public ResponseEntity<Object> authenticatePlayer(
        @RequestBody @Valid AuthenticatePlayerDTO authenticatePlayerDTO,
        @ApiIgnore HttpSession session
    )
    {
        return playerService.authenticatePlayer(authenticatePlayerDTO, session);
    }

    @ApiOperation(
        value = "logout",
        notes = "log out player"
    )
    @DeleteMapping(value = "/player/authenticate")
    public ResponseEntity<Object> restrictPlayer(
        @ApiIgnore HttpSession session
    )
    {
        return playerService.restrictPlayer(session);
    }

    @ApiOperation(
        value = "authenticate status",
        notes = "Returns HTTP Status Code 200 if the user is authenticated"
    )
    @PostMapping(value = "/player/authenticate/status")
    public ResponseEntity<Object> checkPlayerAuthenticateStatus(
        @ApiIgnore HttpSession session
    )
    {
        return ResponseEntity.ok().body(session.getAttribute("player"));
    }
}
