package models.person;

import java.util.List;

import org.junit.Test;

import models.BaseModelTest;
import models.TestDataSource;

@TestDataSource(source = { "person" })
public class PersonTest extends BaseModelTest {

	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testFind() {
		Person person = Person.findByPhone("123456789");
		assertNotNull(person);
	}

	@Test
	public void testFetch() {
		List<Person> personList = Person.fetchAll();
		assertTrue(personList.size() > 0);
	}

}
