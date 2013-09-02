/**
 * 
 */
package fei.persistence.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Giuseppe Diomede
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "Address")
public class Address extends DataObjectRoot {


	@Column(name = "Name", nullable = false)
    private String Name;

	
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	public Address(String name) {
		super();
		this.Name = name;
	}

}
