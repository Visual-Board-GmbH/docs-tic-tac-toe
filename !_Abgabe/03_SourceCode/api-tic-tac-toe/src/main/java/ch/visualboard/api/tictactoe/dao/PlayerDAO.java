package ch.visualboard.api.tictactoe.dao;

import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Player")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDAO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String nickname;

    private String username;

    private String password;

    private String salt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "longblob")
    private byte[] tileImage;

    @ManyToMany(mappedBy = "playerDAOS")
    private List<GameDAO> gameDAOS;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Player_has_GameHistory",
        joinColumns = {@JoinColumn(name = "Player_id")},
        inverseJoinColumns = {@JoinColumn(name = "GameHistory_id")})
    private List<GameHistoryDAO> gameHistoryDAOS;
}
