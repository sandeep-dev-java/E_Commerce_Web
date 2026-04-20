package Com.E_Commerce.Project.Repository;

import Com.E_Commerce.Project.model.Category;
import Com.E_Commerce.Project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);


    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pagedetails);
}
