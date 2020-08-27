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
public class LEDRGBMatrixDTO
{
    private int id;
    private boolean available;
}
