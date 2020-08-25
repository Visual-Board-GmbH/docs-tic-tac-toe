package ch.visualboard.api.tictactoe.dao;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "GameHistory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameHistoryDAO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer gameId;

    private String gameName;

    @Column(columnDefinition = "longtext")
    private String gameData;

    @Column(columnDefinition = "longtext")
    private String playerData;

    private Date datePlayed;

    @ManyToMany(mappedBy = "gameHistoryDAOS", fetch = FetchType.EAGER)
    private List<PlayerDAO> playerDAOS;
}
