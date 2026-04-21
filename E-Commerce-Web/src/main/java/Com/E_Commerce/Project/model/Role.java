package Com.E_Commerce.Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@Table(name="Roles")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "Role_Name")
    public User_Role role;

    public Role(User_Role userRole) {
    }
}
