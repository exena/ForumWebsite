package gameinfoweb.gameinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gameinfoweb.gameinfo.excludeannotation.Exclude;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @Exclude
    private List<User> users;

}
