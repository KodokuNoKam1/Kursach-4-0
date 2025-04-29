package by.bsuir.kursach.commercialoffer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import by.bsuir.kursach.commercialoffer.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Должно возвращать Optional<User>
}
