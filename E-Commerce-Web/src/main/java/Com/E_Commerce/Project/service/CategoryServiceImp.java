package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.CategoryDto;
import Com.E_Commerce.Project.DTO.CategoryResponse;
import Com.E_Commerce.Project.Exception.APIException;
import Com.E_Commerce.Project.Exception.ResourceNotFoundException;
import Com.E_Commerce.Project.Repository.CategoryRepo;
import Com.E_Commerce.Project.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private CategoryRepo categoryRepo;
    //private List<Category> categories = new ArrayList<>();
 @Autowired
 private ModelMapper modelMapper;

    //fetch method
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pagedetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage=categoryRepo.findAll(pagedetails);
        List<Category> categories= categoryPage.getContent();
  if(categories.isEmpty())
      throw  new APIException("category is not created Yet");

  List<CategoryDto> categoryDtos=categories.stream()
          .map(category ->
  modelMapper.map(category, CategoryDto.class))
          .toList();
CategoryResponse categoryResponse= new CategoryResponse();
categoryResponse.setContent(categoryDtos);
categoryResponse.setPageNumber(categoryPage.getNumber());
categoryResponse.setLastapage(categoryPage.isLast());
categoryResponse.setTotalPages(categoryPage.getTotalPages());
categoryResponse.setPagesize(categoryPage.getSize());
  categoryResponse.setTotalElements(categoryPage.getTotalElements());
return  categoryResponse;
    }

    //create method
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
       Category category=modelMapper.map(categoryDto,Category.class);
        Category savedCategory= categoryRepo.findByCategoryName(categoryDto.getCategoryName());
        if(savedCategory!=null){
            throw  new APIException("Category with the name "+category.getCategoryName()+"already exists");
        }

      Category savedCategory1=   categoryRepo.save(category);
        return  modelMapper.map(savedCategory1,CategoryDto.class);
    }


     //delete method
    @Override
    public CategoryDto deleteCategory(Long categoryId) {
        Category category=categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
      categoryRepo.delete(category);
        return modelMapper.map(category,CategoryDto.class);
    }

    //update method
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Optional<Category> savedCategoryOptional= categoryRepo.findById(categoryId);
        Category savedCategory=
                savedCategoryOptional
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Category","categoryId",categoryId));
        Category category= modelMapper.map(categoryDto,Category.class);
        category.setCategoryId(categoryId);
        savedCategory=categoryRepo.save(category);
        return modelMapper.map(savedCategory,CategoryDto.class);
    }
}
