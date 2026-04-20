package Com.E_Commerce.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;
    private String  productName;
    private String description;
    private Double price;
    private Integer quantity;
    private String image;
    private Double specialprice;
    private Double discount;

}
