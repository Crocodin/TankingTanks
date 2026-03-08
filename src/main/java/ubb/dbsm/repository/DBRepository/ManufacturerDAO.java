package ubb.dbsm.repository.DBRepository;

import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.service.ManufacturerService;
import ubb.dbsm.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManufacturerDAO implements IRepository<Integer,  Manufacturer> {
    private static final DatabaseManager databaseManager = new DatabaseManager();
    private final CountryDAO countryDAO = new CountryDAO();

    @Override
    public Optional<Manufacturer> save(Manufacturer entity) {
        String sql = "INSERT INTO manufacturer(manufacturer_name, founded_year, headquarters_country_id) VALUES (?, ?, ?)";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getYearOfFounding());
            ps.setInt(3, entity.getCountry().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            String error = "Manufacturer save error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public Optional<Manufacturer> update(Manufacturer entity) {
        String sql = "UPDATE manufacturer SET manufacturer_name = ?, founded_year = ?, headquarters_country_id = ? WHERE manufacturer_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getYearOfFounding());
            ps.setInt(3, entity.getCountry().getId());
            ps.setInt(4, entity.getId());

            ps.executeUpdate();

            return Optional.of(entity);
        } catch (SQLException e) {
            String error = "Manufacturer update error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public void delete(Manufacturer entity) {
        String sql = "DELETE FROM manufacturer WHERE id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            String error = "Manufacturer delete error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error);
        }
    }

    @Override
    public Optional<Manufacturer> find(Integer integer) {
        String sql = "SELECT * FROM manufacturer WHERE manufacturer_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int countryId = rs.getInt("headquarters_country_id");
                var country = countryDAO.find(countryId);

                if (country.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(new Manufacturer(rs, country.get()));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String error = "Manufacturer find error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public List<Manufacturer> findAll() {
        String sql = "SELECT * FROM manufacturer";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery();
             List<Manufacturer> manufacturers = new ArrayList<>();

             while (rs.next()) {
                 int countryId = rs.getInt("headquarters_country_id");
                 var country = countryDAO.find(countryId);
                 if (country.isEmpty()) {
                     throw  new DatabaseError("country not found");
                 }
                 manufacturers.add(new Manufacturer(rs, country.get()));
             }
             return manufacturers;
        } catch (SQLException e) {
            String error = "Manufacturer findAll error: " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }

    public Optional<Manufacturer> findByName(String name) {
        String sql = "SELECT * FROM manufacturer WHERE manufacturer_name = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int countryId = rs.getInt("headquarters_country_id");
                var country = countryDAO.find(countryId);

                if (country.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(new Manufacturer(rs, country.get()));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String error = "Couldn't find Manufacturer with the name " + name + ": " + e.getMessage();
            System.out.println(error);
            throw new DatabaseError(error, e);
        }
    }
}
