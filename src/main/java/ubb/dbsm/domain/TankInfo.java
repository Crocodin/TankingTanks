package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tank_info")
public class TankInfo implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tank_info_id")
    private int id;

    @Column(name = "about")
    private String about;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tank_id")
    private Tank tank;

    @Override
    public Integer getId() { return id; }
}
