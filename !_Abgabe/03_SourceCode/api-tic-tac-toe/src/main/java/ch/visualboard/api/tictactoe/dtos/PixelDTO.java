package ch.visualboard.api.tictactoe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PixelDTO
{
    private final int xPosition;
    private final int yPosition;
    private final int rgb;
}
