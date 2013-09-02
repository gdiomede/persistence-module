package fei.persistence.core.repository;

import org.springframework.data.repository.CrudRepository;

import fei.persistence.core.jpa.ObjectID;
import fei.persistence.core.model.DataObjectRoot;

/**
 * Specifies methods used to obtain and modify DataObjectRoot related information
 * which is stored in the database.
 * @author Giuseppe Diomede
 * @version 1.0.0
 */

public interface DataObjectRootRepository extends CrudRepository<DataObjectRoot, ObjectID> {
}
