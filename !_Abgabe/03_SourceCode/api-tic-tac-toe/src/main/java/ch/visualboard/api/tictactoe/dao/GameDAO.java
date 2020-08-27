package ch.visualboard.api.tictactoe.dao;

import ch.visualboard.api.tictactoe.entities.State;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

@Entity(name = "Game")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDAO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(columnDefinition = "ENUM('OPEN', 'PENDING', 'ACTIVE')")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean usingMatrix;

    @Column(columnDefinition = "longtext")
    private String gameData;

    private Date lastModified;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "Player_has_Game",
        joinColumns = {@JoinColumn(name = "Game_id")},
        inverseJoinColumns = {
            @JoinColumn(name = "Player_id")})
    private List<PlayerDAO> playerDAOS;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "Game_has_LEDRGBMatrix",
        joinColumns = {@JoinColumn(name = "Game_id")},
        inverseJoinColumns = {@JoinColumn(name = "LEDRGBMatrix_id")})
    private List<LEDRGBMatrixDAO> ledrgbMatrixDAOS;
}
