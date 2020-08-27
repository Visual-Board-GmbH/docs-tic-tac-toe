package ch.visualboard.api.tictactoe.repositories;

import ch.visualboard.api.tictactoe.dao.LEDRGBMatrixDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LEDRGBMatrixRepository extends JpaRepository<LEDRGBMatrixDAO, Integer>
{
}
