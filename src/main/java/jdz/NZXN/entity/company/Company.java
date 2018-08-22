
package jdz.NZXN.entity.company;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "companies")
@Entity
@ToString
@EqualsAndHashCode(of = { "id" })
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	@Id
	@Getter
	private String id;
	@Getter
	private String name;
	@Getter
	private String url;

	public Company(String id) {
		this.id = id;
	}
}
