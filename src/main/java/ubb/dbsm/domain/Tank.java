package ubb.dbsm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Tank implements HasID<Integer>{
    private int id;
    private String name;
    private int yearOfProduction;
    private Manufacturer manufacturer;

    public Tank(ResultSet rs, Manufacturer manufacturer) throws SQLException {
        this.id = rs.getInt("tank_id");
        this.name = rs.getString("tank_name");
        this.yearOfProduction = rs.getInt("made_date_year");
        this.manufacturer = manufacturer;
    }

    public Tank(String name, int yearOfProduction, Manufacturer manufacturer) {
        this.name = name;
        this.yearOfProduction = yearOfProduction;
        this.manufacturer = manufacturer;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
