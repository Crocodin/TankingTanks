package ubb.dbsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ubb.dbsm.domain.User;
import ubb.dbsm.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User authenticate(String username, String password) {
        log.info("Authenticating user {} with password {}", username, password);
        return userRepository.findUserByUsernameAndPassword(username, password).orElseThrow();
    }
}
