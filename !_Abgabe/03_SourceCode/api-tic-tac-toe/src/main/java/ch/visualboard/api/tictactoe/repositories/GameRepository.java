package ch.visualboard.api.tictactoe.repositories;

import ch.visualboard.api.tictactoe.dao.GameDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameDAO, Integer>
{
}
