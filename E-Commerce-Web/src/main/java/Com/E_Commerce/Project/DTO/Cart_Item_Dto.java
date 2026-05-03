package Com.E_Commerce.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cart_Item_Dto {
    private Long cartItemId;
    private CartDTO cart;
    private ProductDto productDto;
    private Integer quantity;
    private Double discount;
    private Double productPrice;

}
