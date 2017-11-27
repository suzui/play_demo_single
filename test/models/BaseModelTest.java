
package models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import play.test.Fixtures;
import play.test.UnitTest;

@Ignore
@TestDataSource
public class BaseModelTest extends UnitTest {

	@Before
	public void setUp() {
		Class clazz = this.getClass();
		TestDataSource testDataSource = (TestDataSource) clazz.getAnnotation(TestDataSource.class);
		if (testDataSource == null) {
			return;
		}
		if (testDataSource.source().length == 0) {
			Fixtures.loadModels("datas/" + clazz.getSimpleName().replace("Test", "") + ".yml");
		} else {
			List<String> sources = Arrays.asList(testDataSource.source());
			Fixtures.loadModels(sources.stream().map(s -> s = "datas/" + s + ".yml").collect(Collectors.toList()));
		}
	}

	@After
	public void setDown() {
		Fixtures.deleteDatabase();
	}
}
