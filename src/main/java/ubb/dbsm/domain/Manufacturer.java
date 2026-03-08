package ubb.dbsm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
@ToString
public class Manufacturer implements HasID<Integer> {
    @Setter
    private int id;
    private String name;
    private int yearOfFounding;
    private Country country;

    public Manufacturer(ResultSet rs, Country country) throws SQLException {
        this.id = rs.getInt("manufacturer_id");
        this.name = rs.getString("manufacturer_name");
        this.yearOfFounding = rs.getInt("founded_year");
        this.country = country;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
