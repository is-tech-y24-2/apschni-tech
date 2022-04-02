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
public class Cat {
    @Id
    @GeneratedValue
    private UUID id;

    public Cat(String name, LocalDate birthdate, String breed, Color color, Owner owner) {
        this.name = name;
        this.birthdate = birthdate;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        this.friends = new ArrayList<>();
    }

    private String name;

    private LocalDate birthdate;

    private String breed;

    @Enumerated
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "cat_friend",
            joinColumns = { @JoinColumn(name = "cat_id") },
            inverseJoinColumns = { @JoinColumn(name = "friend_id") }
    )
    private List<Cat> friends;
}
