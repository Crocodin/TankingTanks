package ubb.dbsm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.TankRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class OptimisticLockingTest {

    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Integer testTankId;

    @BeforeEach
    void setUp() {
        // Insert a fresh tank before each test
        Tank tank = Tank.builder()
                .name("Test Tank")
                .build();
        testTankId = tankRepository.saveAndFlush(tank).getId();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        tankRepository.deleteById(testTankId);
    }

    @Test
    void whenTwoUsersUpdateSameRecord_thenSecondUpdateShouldFail() {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        // User A and User B both read the same record (version = 0)
        Tank userA = tankRepository.findById(testTankId).orElseThrow();
        Tank userB = tankRepository.findById(testTankId).orElseThrow();

        log.info("OPTIMISTIC LOCKING DEMO");
        log.info("[User A] Loaded tank: '{}' | version = {}", userA.getName(), userA.getVersion());
        log.info("[User B] Loaded tank: '{}' | version = {}", userB.getName(), userB.getVersion());

        // User A saves successfully in their own transaction (version becomes 1)
        Tank savedByA = tx.execute(status -> {
            userA.setName("Updated by User A");
            return tankRepository.saveAndFlush(userA);
        });

        Assertions.assertNotNull(savedByA);
        log.info("[User A] Saved successfully! | version = {}", savedByA.getVersion());

        // User B tries to save with stale version (still version = 0)
        log.info("[User B] Attempting to save with stale version = {}...", userB.getVersion());
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            tx.execute(status -> {
                userB.setName("Updated by User B");
                return tankRepository.saveAndFlush(userB);
            });
        });
        log.warn("[User B] CONFLICT DETECTED! Update rejected by the database.");
        log.info("TEST PASSED");
    }
}