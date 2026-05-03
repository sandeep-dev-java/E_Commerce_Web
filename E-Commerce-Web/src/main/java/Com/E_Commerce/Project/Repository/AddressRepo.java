package Com.E_Commerce.Project.Repository;

import Com.E_Commerce.Project.model.Address;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo  extends JpaRepository<Address, Long> {
}
