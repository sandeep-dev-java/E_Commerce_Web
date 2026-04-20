package Com.E_Commerce.Project.controller;

import Com.E_Commerce.Project.ConFig.AppConstant;
import Com.E_Commerce.Project.DTO.CategoryDto;
import Com.E_Commerce.Project.DTO.CategoryResponse;
import Com.E_Commerce.Project.model.Category;
import Com.E_Commerce.Project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/echo")
    public ResponseEntity<String> echo(@RequestParam(name="message") String message){
        return  new ResponseEntity<>("Echoed message "+ message,HttpStatus.OK);
    }
    @GetMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(
     @RequestParam(name="pageNumber",defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
      @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
     @RequestParam(name="SortBY",defaultValue = AppConstant.SORT_BY_Category, required = false) String sortBy,
     @RequestParam(name="SortDir",defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder


    ){
        CategoryResponse  categoryResponse = categoryService. getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new  ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
    //post mapping

    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
       CategoryDto categoryDto1= categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory( @Valid @RequestBody CategoryDto categoryDto,
                                                 @PathVariable Long categoryId){
            CategoryDto savedCategory = categoryService.updateCategory(categoryDto, categoryId);
            return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(@Valid @PathVariable Long categoryId){
            CategoryDto categoryDto = categoryService.deleteCategory(categoryId);
            return  new ResponseEntity<>(categoryDto,HttpStatus.OK);

        }
    }

