package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.ProductDto;
import Com.E_Commerce.Project.DTO.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto addProduct(Long categoryId, ProductDto productDto);

    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchByCategory(Long catgoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByProductKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto updatedProductDto(Long productId, ProductDto productdto);

    ProductDto deleteProduct(Long productId);


    ProductDto updateProductImg(Long productId, MultipartFile image) throws IOException;
}
