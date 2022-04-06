package it.geosolutions.geoserver.rest.decoder;

import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author toben
 */
public class RESTFeatureTypeListTest {

	public RESTFeatureTypeListTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testBuild() throws IOException {
		InputStream is = RESTFeatureTypeListTest.class.getResourceAsStream("/testdata/featureTypeListExample.xml");
		String response = IOUtils.toString(is);
		is.close();
		RESTFeatureTypeList result = RESTFeatureTypeList.build(response);
		List<String> list = result.getNames();

		assertArrayEquals(new String[]{"states", "tasmania_cities", "tasmania_roads", "tasmania_state_boundaries", "tasmania_water_bodies"}
				, list.toArray());
	}
}