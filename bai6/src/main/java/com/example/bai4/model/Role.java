package  com.example.bai4.model;
import jakarta.persistence.*;

import lombok.*;
import jakarta.persistence.Id;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}