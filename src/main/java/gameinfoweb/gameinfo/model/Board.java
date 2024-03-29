package gameinfoweb.gameinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gameinfoweb.gameinfo.excludeannotation.Exclude;
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

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id") //뒤의 refCol은 PK면 생략 가능.
    @JsonIgnore
    @Exclude
    private User user;

    public Image getThumbnailImage(){
        for(Image img:images){
            if(img.getFileName().matches("^s_.+")){
                return img;
            }
        }
        return null;
    }
}
