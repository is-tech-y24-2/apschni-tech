package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    private String password;

    @Enumerated
    private Role role;

    private Boolean isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Owner owner;

    public User(String username, String password, Role role, Owner owner) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.owner = owner;
        this.isActive = true;
    }
}
