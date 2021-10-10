package gameinfoweb.gameinfo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min=2,max=30)
    private String title;
    private String content;

    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();
}
