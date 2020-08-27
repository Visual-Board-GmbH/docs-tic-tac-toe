package ch.visualboard.api.tictactoe.entities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ResourceUtils;

@Getter
@AllArgsConstructor
public enum TicTacToeTile
{
    X("classpath:images/x.png"),
    O("classpath:images/o.png");

    private final String imageUrl;

    public static BufferedImage getBufferedImageForTile(TicTacToeTile tile) throws IOException
    {
        if (tile == X) {
            return ImageIO.read(ResourceUtils.getFile(X.getImageUrl()));
        }

        return ImageIO.read(ResourceUtils.getFile(O.getImageUrl()));
    }
}
