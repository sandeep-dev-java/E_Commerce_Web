package Com.E_Commerce.Project.Repository;

import Com.E_Commerce.Project.model.Role;
import Com.E_Commerce.Project.model.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role ,Long> {

   Optional<Role>  findByRole(User_Role role);;
}
