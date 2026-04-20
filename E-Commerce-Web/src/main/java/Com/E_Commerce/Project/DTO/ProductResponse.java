package Com.E_Commerce.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private List<ProductDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long TotalElemnets;
    private Integer totalPages;
    private boolean lastPage;


}
