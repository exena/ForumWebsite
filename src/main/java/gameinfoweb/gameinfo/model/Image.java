package gameinfoweb.gameinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gameinfoweb.gameinfo.excludeannotation.Exclude;
import lombok.Data;

import javax.persistence.*;

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
    @JsonIgnore
    @Exclude
    private Board board;
}
