package ch.visualboard.api.tictactoe.repositories;

import ch.visualboard.api.tictactoe.dao.PlayerDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerDAO, Integer>
{
    PlayerDAO findByUsername(String username);

    PlayerDAO findByNickname(String nickname);
}
