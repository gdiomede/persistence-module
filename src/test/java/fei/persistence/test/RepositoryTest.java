package fei.persistence.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fei.persistence.core.model.User;
import fei.persistence.core.repository.DataObjectRootRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:META-INF/application-context.xml")
public class RepositoryTest {

	@Autowired
	DataObjectRootRepository dataObjectRootService;
 
	
	@Test
    public void test() {
//		DataObjectRoot root = new DataObjectRoot();
//		dataObjectRootService.save(root);
		User user = new User("Giuseppe", "Diomede");
		dataObjectRootService.save(user);
		System.out.println("done..!");
    }
}
