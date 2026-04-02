package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "manufacturer")
public class Manufacturer implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private int id;

    @Column(name = "manufacturer_name")
    private String name;

    @Column(name = "founded_year")
    private int yearOfFounding;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "headquarters_country_id")
    private Country country;

    @Override
    public Integer getId() {
        return id;
    }
}
