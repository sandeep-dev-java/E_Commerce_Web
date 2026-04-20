package Com.E_Commerce.Project.Repository;

import Com.E_Commerce.Project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findByCategoryName(String name);
}
