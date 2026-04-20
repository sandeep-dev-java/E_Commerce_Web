package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.CategoryDto;
import Com.E_Commerce.Project.DTO.CategoryResponse;
import Com.E_Commerce.Project.model.Category;
import com.sun.jdi.InterfaceType;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String SortOrder);
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
