
package jdz.NZXN.entity.company;

import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, String> {
	public Company findByCompanyId(String companyId);
}
