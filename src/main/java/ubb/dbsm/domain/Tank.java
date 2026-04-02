package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tank")
public class Tank implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tank_id")
    private int id;

    @Column(name = "tank_name")
    private String name;

    @Column(name = "made_date_year")
    private int yearOfProduction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Override
    public Integer getId() {
        return id;
    }
}
