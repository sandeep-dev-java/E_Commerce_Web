package Com.E_Commerce.Project.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Users",
        uniqueConstraints =
          {@UniqueConstraint(columnNames = "UserName")
        , @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long User_id ;
    @NotBlank
    @Email
    private String email;
    @NotBlank @NotNull
    @Size(max=20)
    private String userName;
@NotBlank
@Size(min = 8 ,max= 350)
private String password;

    public User(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
                         fetch = FetchType.EAGER)
    @JoinTable(name = "User_role", joinColumns = @JoinColumn(name = "User_id"),
                    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE}
            ,orphanRemoval = true
    )
    private Set<Product> products= new HashSet<>();
    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="user_address",
    joinColumns = @JoinColumn(name = "user_id")
    ,inverseJoinColumns = @JoinColumn(referencedColumnName = "addressId"))
 private List<Address> addresses= new ArrayList<>();

}