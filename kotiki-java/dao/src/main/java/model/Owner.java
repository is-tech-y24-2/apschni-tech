package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    public Owner(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
        this.cats = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private LocalDate birthdate;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Cat> cats;
}
