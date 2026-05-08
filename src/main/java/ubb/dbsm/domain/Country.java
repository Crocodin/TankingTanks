package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country_name")
    private String name;

    @Column(name = "gdp")
    private float gdp;

    @Override
    public Integer getId() { return id; }
}
