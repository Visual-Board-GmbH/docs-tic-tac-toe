package ch.visualboard.api.tictactoe.dtos;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdatePlayerDTO
{
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String nickname;
    @NotNull
    private String password;
}
