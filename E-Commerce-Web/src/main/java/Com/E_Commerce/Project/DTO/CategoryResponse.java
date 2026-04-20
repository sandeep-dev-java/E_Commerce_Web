package Com.E_Commerce.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryDto> content;
    private  Integer pageNumber;
    private Integer pagesize;
    private  Long totalElements;
    private Integer totalPages;
    private boolean lastapage;
}
