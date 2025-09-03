package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private String zoom;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        if (course != null) {
            course.setTeacher(this);
        }
    }
}
