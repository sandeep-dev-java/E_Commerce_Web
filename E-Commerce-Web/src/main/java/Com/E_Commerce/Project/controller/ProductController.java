package Com.E_Commerce.Project.controller;

import Com.E_Commerce.Project.ConFig.AppConstant;
import Com.E_Commerce.Project.DTO.CategoryDto;
import Com.E_Commerce.Project.DTO.ProductDto;
import Com.E_Commerce.Project.DTO.ProductResponse;
import Com.E_Commerce.Project.model.Product;
import Com.E_Commerce.Project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    //Add Product
@PostMapping("/admin/categories/{categoryId}/product")
public ResponseEntity<ProductDto> addProduct(@Valid
        @RequestBody ProductDto productDto,
        @PathVariable Long categoryId)
{
 ProductDto savedprductDto= productService.addProduct(categoryId,productDto);
   return   new ResponseEntity<>(savedprductDto,HttpStatus.CREATED);

    }

    //GET Product
     @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(name="pagenumber",defaultValue = AppConstant.PAGE_NUMBER ,required = false)Integer pageNumber ,
            @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sorBy",defaultValue = AppConstant.SORT_BY_Product,required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstant.SORT_DIR,required = false)String sortOrder
     ){
    ProductResponse productResponse =productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@Valid @PathVariable Long categoryId,
                                                                @RequestParam(name="pagenumber",defaultValue = AppConstant.PAGE_NUMBER ,required = false)Integer pageNumber ,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pageSize,
                                                                @RequestParam(name="sorBy",defaultValue = AppConstant.SORT_BY_Product,required = false)String sortBy,
                                                                @RequestParam(name="sortOrder",defaultValue = AppConstant.SORT_DIR,required = false)String sortOrder){

      ProductResponse productResponse = productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
      return  new ResponseEntity<>(productResponse,HttpStatus.OK);

}
    @GetMapping("/public/products/keyword/{keyword}")
    ResponseEntity<ProductResponse> getProductsKeyword(
            @PathVariable String keyword,@RequestParam(name="pagenumber",defaultValue = AppConstant.PAGE_NUMBER ,required = false)Integer pageNumber ,
            @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sorBy",defaultValue = AppConstant.SORT_BY_Product,required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstant.SORT_DIR,required = false)String sortOrder){
    ProductResponse productResponse =productService.searchByProductKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
    return  new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }
    @PutMapping("/admin/products/{productId}")
    public  ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productdto,
                                                          @PathVariable Long productId){
    ProductDto updatedProductDto = productService.updatedProductDto(productId,productdto);
    return  new ResponseEntity<>(updatedProductDto,HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{prodcutId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId){
    ProductDto productdto= productService.deleteProduct(productId);
    return  new ResponseEntity<>(productdto,HttpStatus.OK);
    }

    //image update
    @PutMapping("/products/{productId}/image")
    public  ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
                                                          @RequestParam("image")MultipartFile image) throws IOException {
    ProductDto imgproduct= productService.updateProductImg(productId,image);
    return  new ResponseEntity<>(imgproduct,HttpStatus.OK);
    }
}
