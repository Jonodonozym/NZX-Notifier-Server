
package jdz.NZXN.entity.company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {
	public Company findByCompanyId(String companyId);
}
