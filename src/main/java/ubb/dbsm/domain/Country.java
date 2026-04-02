package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
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

    @Column(name = "GDP")
    private Integer gdp;

    @Override
    public Integer getId() {
        return id;
    }
}
