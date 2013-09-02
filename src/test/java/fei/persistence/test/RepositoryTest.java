package fei.persistence.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fei.persistence.core.model.Address;
import fei.persistence.core.model.User;
import fei.persistence.core.service.DataObjectRootService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:META-INF/application-context.xml")
public class RepositoryTest {


	@Autowired
	DataObjectRootService dataObjectRootService;

	
	@Test
    public void test() {
		for (int i = 0; i < 100; i++) {
			User user = new User("Giuseppe"+i, "Diomede"+i);
			Address address = new Address("Via"+i);
			dataObjectRootService.save(user);
			dataObjectRootService.save(address);	
		}
		System.out.println("done..!");
    }
}
