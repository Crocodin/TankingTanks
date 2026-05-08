package ubb.dbsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ubb.dbsm.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
}
