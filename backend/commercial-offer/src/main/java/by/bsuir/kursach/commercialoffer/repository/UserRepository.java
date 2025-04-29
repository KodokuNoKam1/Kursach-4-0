package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}