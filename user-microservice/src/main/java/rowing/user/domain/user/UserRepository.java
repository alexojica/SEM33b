package rowing.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find user by Username.
     */
    Optional<User> findByUserId(String userId);

    /**
     * Check if an existing user already uses a Username.
     */
    boolean existsByUserId(String userId);
}
