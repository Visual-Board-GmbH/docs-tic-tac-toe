package ch.visualboard.api.tictactoe.repositories;

import ch.visualboard.api.tictactoe.dao.GameHistoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistoryDAO, Long>
{
}
