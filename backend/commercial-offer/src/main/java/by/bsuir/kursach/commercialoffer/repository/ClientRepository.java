package by.bsuir.kursach.commercialoffer.repository;

import by.bsuir.kursach.commercialoffer.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}