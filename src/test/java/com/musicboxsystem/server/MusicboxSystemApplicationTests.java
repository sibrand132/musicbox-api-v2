package com.musicboxsystem.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.repository.BandsRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringApplicationConfiguration(classes = MusicboxSystemApplication.class)
@WebAppConfiguration
public class MusicboxSystemApplicationTests {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private BandsRepository bandsRepository;

	@BeforeClass
	public static void setUpBefore() throws Exception{
		//raz przed testami
	}

	@AfterClass
	public static void afterTest() throws Exception{
		//po wykonaniu testów
	}

	@Before
	public void setUp() throws Exception{
		//przed każdym testem
	}

	@After
	public void after() throws Exception{
		//po każdym teście
	}


	@Test
	public void testCreateBands() throws IOException {
		Map<String, Object> requestBody = new HashMap<>();
		//requestBody.put("name", "Name");
		requestBody.put("about", "desc");
		requestBody.put("established", "01.01.2017");
		requestBody.put("leader", "Admin");
		requestBody.put("status", "active");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> httpEntity = new HttpEntity<>(OBJECT_MAPPER.writeValueAsString(requestBody), requestHeaders);

		Map<String, Object> apiResponse = restTemplate.postForObject("http://localhost:8080/api/bands/saveBands", httpEntity, Map.class, Collections.emptyMap());
		assertNotNull(apiResponse);

		String message = apiResponse.get("message").toString();
		assertEquals("Success", message);

		String bandsId = ((Map<String,Object>) apiResponse.get("band")).get("id").toString();
		assertNotNull(bandsId);

		Bands bands = bandsRepository.findOne(bandsId);
		assertEquals("Name", bands.getName());
		assertEquals("desc", bands.getAbout());
		assertEquals("01.01.2017", bands.getEstablished());
		assertEquals("Admin", bands.getLeader());
		assertEquals("active", bands.getStatus());



	}

}
