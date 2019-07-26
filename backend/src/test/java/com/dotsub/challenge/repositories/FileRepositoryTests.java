package com.dotsub.challenge.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class FileRepositoryTests {

	@Autowired
	private MockMvc mvc;


	@Test
	public void testUpdateExistingFile() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cable-stayed-bridge.jpeg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		String base64EncodedImage = Base64.getEncoder().encodeToString(picBytes);

		// creating the json representation of a new file
		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "wrong description");
		requestBody.put("title", "wrong title");
		requestBody.put("data", base64EncodedImage);

		// a post request must dispatch a async operation on the server ...
		MvcResult resultActions = mvc.perform(post("/files").content(requestBody.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted()).andReturn();

		// when complete, the http staus code must be 201 and between the response
		// headers must be the location for the newly created resource
		MockHttpServletResponse response = mvc.perform(asyncDispatch(resultActions)).andExpect(status().isCreated())
				.andExpect(header().exists("location")).andReturn().getResponse();

		String resourceLocation = response.getHeader("location");

		var newDescription = "Located at Pedro II Square at Teresina downtown, it is one of the oldest theatres in Teresina";
		var newTitle = "4th of September Theatre";
		File newFile = new File(classLoader.getResource("teatro-4-de-setembro.jpg").getFile());
		byte[] newPicBytes = Files.readAllBytes(newFile.toPath());
		String newBase64EncodedImage = Base64.getEncoder().encodeToString(newPicBytes);

		JSONObject updateBody = new JSONObject();
		updateBody.put("description", newDescription);
		updateBody.put("title", newTitle);
		updateBody.put("data", newBase64EncodedImage);

		// triggering an PUT request, starts a async operation on the server 
		resultActions = mvc.perform(put(resourceLocation).contentType(MediaType.APPLICATION_JSON).content(updateBody.toString())
				.accept(MediaType.APPLICATION_JSON)).andExpect(request().asyncStarted()).andReturn();

		// the update completes with a 204 http code
		mvc.perform(asyncDispatch(resultActions))
			.andExpect(status().isNoContent());

		mvc.perform(get(resourceLocation)).andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8))
			.andExpect(jsonPath("description", is(newDescription)))
			.andExpect(jsonPath("title", is(newTitle))).andExpect(jsonPath("dateCreated").exists())
			.andExpect(jsonPath("data").doesNotExist());

		// in order to obtain the file's data, a second request must be issued to
		// /download/id
		Pattern pattern = Pattern.compile("(\\S+)/files/(\\d+)$");
		Matcher matcher = pattern.matcher(resourceLocation);
		assertTrue(matcher.matches());
		String serverUrl = matcher.group(1);
		int idFIle = Integer.valueOf(matcher.group(2));

		// the request to dowload the file dispatches an async operation on the server
		String downloadEndpoint = serverUrl + "/download/" + idFIle;
		resultActions = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		// ... which returns the correct binary
		mvc.perform(asyncDispatch(resultActions)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(newPicBytes));

	}

	@Test
	public void testCreatingANewFile() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cable-stayed-bridge.jpeg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		String base64EncodedImage = Base64.getEncoder().encodeToString(picBytes);

		// creating the json representation of a new file
		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "Cable stayed bridge in my city");
		requestBody.put("title", "Cable-stayed pic");
		requestBody.put("data", base64EncodedImage);

		// a post request must dispatch a async operation on the server ...
		MvcResult resultActions = mvc.perform(post("/files").content(requestBody.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted()).andReturn();

		// when complete, the http staus code must be 201
		// and between the response headers must be the location for the newly created
		// resource
		MockHttpServletResponse response = mvc.perform(asyncDispatch(resultActions)).andExpect(status().isCreated())
				.andExpect(header().exists("location")).andReturn().getResponse();

		String resourceLocation = response.getHeader("location");

		// here, a get request to the location of the previously created file should
		// return a status code 200 as well as a json body with only the fields
		// description, title and dateCreated
		mvc.perform(get(resourceLocation)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("description", is("Cable stayed bridge in my city")))
				.andExpect(jsonPath("title", is("Cable-stayed pic"))).andExpect(jsonPath("dateCreated").exists())
				.andExpect(jsonPath("data").doesNotExist());

		// in order to obtain the file's data, a second request must be issued to
		// /download/id
		Pattern pattern = Pattern.compile("(\\S+)/files/(\\d+)$");
		Matcher matcher = pattern.matcher(resourceLocation);
		assertTrue(matcher.matches());
		String serverUrl = matcher.group(1);
		int idFIle = Integer.valueOf(matcher.group(2));

		// the request to dowload the file dispatches an async operation on the server
		String downloadEndpoint = serverUrl + "/download/" + idFIle;
		resultActions = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		// ... which returns the correct binary
		mvc.perform(asyncDispatch(resultActions)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(picBytes));

	}

}
