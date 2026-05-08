package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tank_user")
public class User implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Override
    public Integer getId() { return id; }
}
