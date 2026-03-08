package ubb.dbsm.repository.DBRepository;

import lombok.NonNull;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.repository.IRepository;
import ubb.dbsm.repository.TankIRepository;
import ubb.dbsm.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TankDAO implements TankIRepository {
    private static final DatabaseManager databaseManager = new DatabaseManager();
    private final IRepository<Integer,  Manufacturer> manufacturerDAO = new ManufacturerDAO();

    @Override
    public List<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturerScope) {
        String sql = "SELECT * FROM tank WHERE tank_name LIKE ? AND manufacturer_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setInt(2, manufacturerScope.getId());
            return getTanksList(ps);
        } catch (SQLException e) {
            String error = "Error findAll TankDAO: " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @NonNull
    private List<Tank> getTanksList(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Tank> tanks = new ArrayList<>();

        while (rs.next()) {
            int manufacturerId = rs.getInt("manufacturer_id");
            var manufacturer = manufacturerDAO.find(manufacturerId);
            if (manufacturer.isEmpty()) {
                throw new DatabaseError("No manufacturer found with id " + manufacturerId + "for tank with id " + rs.getInt("tank_id"));
            } else {
                tanks.add(new Tank(rs, manufacturer.get()));
            }
        }
        return tanks;
    }

    @Override
    public Optional<Tank> save(Tank entity) throws DatabaseError {
        String sql = "INSERT INTO tank(tank_name, made_date_year, manufacturer_id) VALUES (?, ?, ?)";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getYearOfProduction());
            ps.setInt(3, entity.getManufacturer().getId());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            String error = "Error find TankDAO: " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public Optional<Tank> update(Tank entity) {
        String sql = "UPDATE tank SET tank_name = ?, made_date_year = ?, manufacturer_id = ? WHERE tank_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getYearOfProduction());
            ps.setInt(3, entity.getManufacturer().getId());
            ps.setInt(4, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            String error = "Error find TankDAO: " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
        return Optional.of(entity);
    }

    @Override
    public void delete(Tank entity) {
        String sql = "DELETE FROM tank WHERE tank_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,  entity.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            String error = "Error delete TankDAO " + entity + ": " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public Optional<Tank> find(Integer integer) {
        String sql = "SELECT * FROM tank WHERE tank_id = ?";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, integer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int manufacturerId = rs.getInt("manufacturer_id");
                var manufacturer = manufacturerDAO.find(manufacturerId);
                if (manufacturer.isEmpty()) {
                    throw new DatabaseError("No manufacturer found with id " + manufacturerId);
                }
                return Optional.of(new Tank(rs, manufacturer.get()));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String error = "Error find TankDAO " + integer + ": " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
    }

    @Override
    public List<Tank> findAll() {
        String sql = "SELECT * FROM tank";

        try (Connection conn = databaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            return getTanksList(ps);
        } catch (SQLException e) {
            String error = "Error findAll TankDAO: " + e.getMessage();
            System.err.println(error);
            throw new DatabaseError(error, e);
        }
    }
}
