package fei.persistence.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fei.persistence.core.model.DataObjectRoot;
import fei.persistence.core.repository.DataObjectRootRepository;

/**
 * This implementation of the PersonService interface communicates with
 * the database by using a Spring Data JPA repository.
 * @author Petri Kainulainen
 */
@Service
public class RepositoryDataObjectRootService implements DataObjectRootService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDataObjectRootService.class);
    
    @Resource
    private DataObjectRootRepository dataObjectRootRepository;

    @Transactional
    @Override
    public DataObjectRoot create(DataObjectRoot created) {
        LOGGER.debug("Creating a new DataObjectRoot with information: " + created);
        
        DataObjectRoot dataObjectRoot = new DataObjectRoot();
        
        return (DataObjectRoot) dataObjectRootRepository.save(dataObjectRoot);
    }

    @Transactional(rollbackFor = DataObjectRootNotFoundException.class)
    @Override
    public DataObjectRoot delete(Long id) throws DataObjectRootNotFoundException {
        LOGGER.debug("Deleting DataObjectRoot with id: " + id);
        
        DataObjectRoot deleted = (DataObjectRoot) dataObjectRootRepository.findOne(id);
        
        if (deleted == null) {
            LOGGER.debug("No DataObjectRoot found with id: " + id);
            throw new DataObjectRootNotFoundException();
        }
        
        dataObjectRootRepository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DataObjectRoot> findAll() {
        LOGGER.debug("Finding all DataObjectRoots");
        return dataObjectRootRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public DataObjectRoot findById(Long id) {
        LOGGER.debug("Finding DataObjectRoot by id: " + id);
        return (DataObjectRoot) dataObjectRootRepository.findOne(id);
    }

    @Transactional(rollbackFor = DataObjectRootNotFoundException.class)
    @Override
    public DataObjectRoot update(DataObjectRoot updated) throws DataObjectRootNotFoundException {
        LOGGER.debug("Updating DataObjectRoot with information: " + updated);
        
        DataObjectRoot dataObjectRoot = (DataObjectRoot) dataObjectRootRepository.findOne(updated.getId());
        
        if (dataObjectRoot == null) {
            LOGGER.debug("No person found with id: " + updated.getId());
            throw new DataObjectRootNotFoundException();
        }
        
        //dataObjectRoot.update();

        return dataObjectRoot;
    }

    /**
     * This setter method should be used only by unit tests.
     * @param DataObjectRootRepository
     */
    protected void setDataObjectRootRepository(DataObjectRootRepository dataObjectRootRepository) {
        this.dataObjectRootRepository = dataObjectRootRepository;
    }
}
