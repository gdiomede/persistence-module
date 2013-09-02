package fei.persistence.core.service;

import java.util.List;

import fei.persistence.core.jpa.ObjectID;
import fei.persistence.core.model.DataObjectRoot;

/**
 * Declares methods used to obtain and modify DataObjectRoot information.
 * @author Giuseppe Diomede
 * @version 1.0.0
 */
public interface DataObjectRootService {

    /**
     * Creates a new DataObjectRoot.
     * @param created   The information of the created DataObjectRoot.
     * @return The created DataObjectRoot.
     */
    public DataObjectRoot create(DataObjectRoot created);

    /**
     * Deletes a DataObjectRoot.
     * @param id	The id of the deleted DataObjectRoot.
     * @return The deleted DataObjectRoot.
     * @throws DataObjectRootNotFoundException  if no DataObjectRoot is found with the given id.
     */
    public DataObjectRoot delete(ObjectID id) throws DataObjectRootNotFoundException;

    /**
     * Finds all DataObjectRoots.
     * @return A list of DataObjectRoots.
     */
    public List<DataObjectRoot> findAll();

    /**
     * Finds DataObjectRoot by id.
     * @param id	The id of the wanted DataObjectRoot.
     * @return The found DataObjectRoot. If no DataObjectRoot is found, this method returns null.
     */
    public DataObjectRoot findById(ObjectID id);

    /**
     * Updates the information of a DataObjectRoot.
     * @param updated	The information of the updated DataObjectRoot.
     * @return The updated DataObjectRoot.
     * @throws DataObjectRootNotFoundException if no DataObjectRoot is found with given id.
     */
    public DataObjectRoot update(DataObjectRoot updated) throws DataObjectRootNotFoundException;

    
    /**
     * Saves the information of a DataObjectRoot.
     * @param toSave	The information of the DataObjectRoot that has to be saved.
     * @return The saved DataObjectRoot.
     */

    public DataObjectRoot save(DataObjectRoot toSave);
    
}
