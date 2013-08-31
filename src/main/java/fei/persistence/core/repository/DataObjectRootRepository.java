package fei.persistence.core.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import fei.persistence.core.model.DataObjectRoot;

/**
 * Specifies methods used to obtain and modify DataObjectRoot related information
 * which is stored in the database.
 * @author Giuseppe Diomede
 * @version 1.0.0
 */

public interface DataObjectRootRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	
	@Override
	public <S extends T> S save(S arg0);
	
}
