package app.entities;

import app.enums.CourseName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseName courseName;
    private String description;
    private LocalDate endDate;
    private LocalDate startDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    private Teacher teacher;

    public void addStudent(Student student) {
        this.students.add(student);
        if (student != null) {
            student.setCourse(this);
        }
    }
}
