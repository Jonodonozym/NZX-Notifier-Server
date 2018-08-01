
package jdz.NZXN.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jdz.NZXN.entity.company.Company;
import jdz.NZXN.entity.company.CompanyRepository;

@Controller
@RequestMapping("/companies")
public class CompaniesController {
	@Autowired private CompanyRepository repo;
	
	@GetMapping
	public List<Company> fetchAll() {
		return repo.findAll();
	}
	
	@GetMapping("/search")
	public List<Company> search(@RequestParam String query){
		return repo.findByIdStartingWith(query);
	}
}
