
package jdz.NZXN.entity.company;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {
	public Optional<Company> findById(String companyId);
	public List<Company> findByIdStartingWith(String companyId);
}
