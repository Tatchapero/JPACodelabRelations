package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private LocalDateTime updatedAt;

    @ManyToOne
    @ToString.Exclude
    private Course course;

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
}
