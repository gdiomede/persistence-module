/**
 * 
 */
package fei.persistence.core.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * @author gdiomede
 *
 */
public class DataObjectRootRepositoryImpl<T, ID extends Serializable>
	extends SimpleJpaRepository<T, ID> implements DataObjectRootRepository<T, ID> {

	private EntityManager entityManager;
	
	

	// There are two constructors to choose from, either can be used.
	public DataObjectRootRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		// This is the recommended method for accessing inherited class dependencies.
		this.entityManager = entityManager;
	}
	
	@Override
	public <S extends T> S save(S entity) {
		// TODO Auto-generated method stub
		return super.save(entity);
	}
}