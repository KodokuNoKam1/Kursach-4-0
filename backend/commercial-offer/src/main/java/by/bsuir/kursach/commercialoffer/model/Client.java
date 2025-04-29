package by.bsuir.kursach.commercialoffer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String company;

    @Column
    private String email;

    @Column
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}