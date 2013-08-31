/**
 * 
 */
package fei.persistence.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Giuseppe Diomede
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "GDUser")
public class User extends DataObjectRoot {


	@Column(name = "Name", nullable = false)
    private String Name;

    @Column(name = "LastName", nullable = false)
    private String lastName;

	
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

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User(String name, String lastName) {
		super();
		Name = name;
		this.lastName = lastName;
	}

}
