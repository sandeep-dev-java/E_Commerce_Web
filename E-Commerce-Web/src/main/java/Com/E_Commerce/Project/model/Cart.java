package Com.E_Commerce.Project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.TrueFalseConverter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name= "carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
     @OneToMany(mappedBy = "cart",
     cascade = {CascadeType.PERSIST,CascadeType.MERGE},
             orphanRemoval= true)
    private List<CartItem> cartItems= new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private  Double totalPrice=0.0;

}
