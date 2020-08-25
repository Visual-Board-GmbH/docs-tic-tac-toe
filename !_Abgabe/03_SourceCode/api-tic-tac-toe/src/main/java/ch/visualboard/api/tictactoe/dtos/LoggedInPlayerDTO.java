package ch.visualboard.api.tictactoe.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInPlayerDTO
{
    private int id;
    private String name;
    private String username;
    private String nickname;
}
