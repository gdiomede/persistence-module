package fei.persistence.core.model;



import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


import fei.persistence.core.jpa.ObjectID;

/**
 * The root entity class. Each subclass will specify the entitity related information.
 * @author Giuseppe Diomede
 * @version 1.0.0
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "DataObjectRoot")
public class DataObjectRoot {
    
	@Id @GeneratedValue(generator="hibseq", strategy=GenerationType.TABLE)
	@GenericGenerator(name="hibseq", strategy = "fei.persistence.core.jpa.ObjectIDGenerator",
	    parameters = {
	        @Parameter(name="table", value = "ClassId"),
	        @Parameter(name="primary_key_column", value = "ClassName"),
	        @Parameter(name="primary_key_id_column", value = "ClassId"),
	        @Parameter(name="value_column", value = "NextIdToUse")
	    }
	) 
	
	private ObjectID id;

    @Version
    @Column(name = "Version", nullable = false)
    private long version = 0;

    @Version
    @Column(name = "Active", nullable = false)
    private boolean active = true;

    
    @Column(name = "TimeCreated", nullable = false)
    private Date timeCreated;

    @Column(name = "TimeUpdated", nullable = false)
    private Date timeUpdated;

    
        
    /**
	 * @return the id
	 */
	public ObjectID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ObjectID id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the timeCreated
	 */
	public Date getTimeCreated() {
		return timeCreated;
	}

	/**
	 * @param timeCreated the timeCreated to set
	 */
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	/**
	 * @return the timeUpdated
	 */
	public Date getTimeUpdated() {
		return timeUpdated;
	}

	/**
	 * @param timeUpdated the timeUpdated to set
	 */
	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	@PreUpdate
    public void preUpdate() {
        timeUpdated = new Date();
    }
    
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        timeCreated = now;
        timeUpdated = now;
    }
}
