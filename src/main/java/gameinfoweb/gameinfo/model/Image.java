package gameinfoweb.gameinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String originalName;

    @ManyToOne
    @JoinColumn(name="board_id", referencedColumnName = "id")
    private Board board;
}
