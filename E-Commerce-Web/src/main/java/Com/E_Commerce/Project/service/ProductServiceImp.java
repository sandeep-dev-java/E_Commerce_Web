package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.ProductDto;
import Com.E_Commerce.Project.DTO.ProductResponse;
import Com.E_Commerce.Project.Exception.APIException;
import Com.E_Commerce.Project.Exception.ResourceNotFoundException;
import Com.E_Commerce.Project.Repository.CategoryRepo;
import Com.E_Commerce.Project.Repository.ProductRepository;
import Com.E_Commerce.Project.model.Category;
import Com.E_Commerce.Project.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
  @Autowired
   private ProductRepository productRepository;
    @Autowired
   private CategoryRepo categoryRepo;
   @Autowired
    private ModelMapper modelMapper;
   @Autowired
   private FileService fileService;
   @Value("${project.image}")
   private String path;
    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) {
       Category category=categoryRepo.findById(categoryId)
       .orElseThrow
               (
          ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
                   );
       boolean isProductNotPresent=true;
       List<Product> products= category.getProducts();
       for(int i=0;i< products.size();i++){
           if(products.get(i).getProductName().equals(productDto.getProductName())){
              isProductNotPresent=false;
              break;
           }
       }
   if(isProductNotPresent){Product product= modelMapper.map(productDto,Product.class);
       product.setImage("default.png");
       product.setCategory(category);
       double specialPrice =product.getPrice() - (product.getDiscount() * 0.01)* product.getPrice();
       product.setSpecialPrice(specialPrice);
       Product savedProduct= productRepository.save(product);
       return  modelMapper.map(savedProduct,ProductDto.class);}
   else {
       throw  new APIException("Product is already present");
   }

    }

    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

       Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pagedetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findAll(pagedetails);
        List<Product > products=productPage.getContent();// it will store  the list of product becz findall give you List<T>
        List<ProductDto> productDtos= products.stream()               // here we convert list of productDto
                .map((product)->  modelMapper.map(product,ProductDto.class)).toList();

        /*ProductResponse productResponse= new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse; beginner friendly */
        return  ProductResponse.builder().content(productDtos).pageNumber(productPage.getNumber()).pageSize(productPage.getSize()).TotalElemnets(productPage.getTotalElements()).lastPage(productPage.isLast()).totalPages(productPage.getTotalPages()).build();// advance friendly
    }

    @Override
    public ProductResponse searchByCategory(Long catgoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category= categoryRepo.findById(catgoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","catgoryId",catgoryId));

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pagedetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findByCategoryOrderByPriceAsc(category,pagedetails);
        List<Product > products=productPage.getContent();
        if(products.isEmpty()){
            throw  new APIException("Product is not found");
        }
        List<ProductDto> productDtos=products .stream()               // here we convert list of productDto
                .map((product)->  modelMapper.map(product,ProductDto.class)).toList();

        if(products.isEmpty()){
            throw new APIException("products is not exist yet");
        }
        return ProductResponse.builder().content(productDtos).pageNumber(productPage.getNumber()).pageSize(productPage.getSize()).TotalElemnets(productPage.getTotalElements()).lastPage(productPage.isLast()).totalPages(productPage.getTotalPages()).build();
    }

    @Override
    public ProductResponse searchByProductKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pagedetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pagedetails);
       List<Product> products= productPage.getContent();
        List<ProductDto> productDtos=products .stream()               // here we convert list of productDto
                .map((product)->  modelMapper.map(product,ProductDto.class)).toList();
       if(products.isEmpty()){
           throw  new APIException("product is not found with this "+keyword);
       }
        return ProductResponse.builder().content(productDtos).pageNumber(productPage.getNumber()).pageSize(productPage.getSize()).TotalElemnets(productPage.getTotalElements()).lastPage(productPage.isLast()).totalPages(productPage.getTotalPages()).build();
    }

    @Override
    public ProductDto updatedProductDto(Long productId, ProductDto productdto) {

        Product pro= productRepository.findById(productId).orElseThrow(()->
         new ResourceNotFoundException("product","productId",productId));

        pro.setProductName(productdto.getProductName());
        pro.setDescription(productdto.getDescription());
        pro.setDiscount(productdto.getDiscount());
        pro.setQuantity(productdto.getQuantity());
        pro.setPrice(productdto.getPrice());
        double specialPrice =productdto.getPrice() - (productdto.getDiscount() * 0.01)* productdto.getPrice();
        pro.setSpecialPrice(specialPrice);
        return modelMapper.map(pro,ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(Long productId) {
       Product product= productRepository.findById(productId).
               orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProductImg(Long productId, MultipartFile image) throws IOException {
        //get the product
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));

        //Uplodad the image to server
        //get the file name of uploaded image
        //UpdATING THE  new file name to product
        String filename= fileService.uploadImage(path,image);
        product.setImage(filename);
        Product updatedproduct=productRepository.save(product);

        // return dto AFTER MAPPING PROCUVT TO DTO
        return modelMapper.map(updatedproduct,ProductDto.class);
    }

}

