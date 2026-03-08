package ubb.dbsm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
@ToString
public class Country implements HasID<Integer> {
    private int id;
    private String name;

    public Country(ResultSet rs) throws SQLException {
        this.id = rs.getInt("country_id");
        this.name = rs.getString("country_name");
    }

    @Override
    public Integer getId() {
        return id;
    }
}
