package ubb.dbsm.repository.DBRepository;

import ubb.dbsm.domain.Country;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryDAO implements IRepository<Integer, Country> {
    private static final DatabaseManager databaseManager = new DatabaseManager();

    @Override
    public Optional<Country> save(Country entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Country> update(Country entity) {
        return Optional.empty();
    }

    @Override
    public void delete(Country entity) {

    }

    @Override
    public Optional<Country> find(Integer integer) {
        String sql = "SELECT * FROM country WHERE country_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new Country(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            String error = "Country find error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public List<Country> findAll() {
        String sql = "SELECT * FROM country";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Country> countries = new ArrayList<>();

            while (rs.next()) {
                countries.add(new Country(rs));
            }

            return countries;

        } catch (SQLException e) {
            String error = "Country findAll error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }
}
