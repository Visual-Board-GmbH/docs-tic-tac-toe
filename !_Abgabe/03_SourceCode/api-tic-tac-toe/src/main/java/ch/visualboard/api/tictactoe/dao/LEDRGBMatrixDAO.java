package ch.visualboard.api.tictactoe.dao;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity(name = "LEDRGBMatrix")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LEDRGBMatrixDAO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean available;

    @ManyToMany(mappedBy = "ledrgbMatrixDAOS")
    private List<GameDAO> gameDAOS;
}
