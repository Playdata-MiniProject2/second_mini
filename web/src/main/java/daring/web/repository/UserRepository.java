package daring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import daring.web.domain.User;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
}
