package com.dotsub.challenge.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class FileRepositoryPATCHTests {

	@Autowired
	private MockMvc mvc;
	private String resourceLocation;
	private String downloadEndpoint;

	private static final String ORIGINAL_DESCRIPTION = "foo description xpto";
	private static final String ORIGINAL_TITLE = "foo bar title";
	private static String ORIGINAL_BASE64_ENCODED_IMAGE;

	@BeforeClass
	public static void loadBase64EncodedImage() throws IOException {
		ClassLoader classLoader = FileRepositoryPATCHTests.class.getClassLoader();
		File file = new File(classLoader.getResource("cable-stayed-bridge.jpeg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		ORIGINAL_BASE64_ENCODED_IMAGE = Base64.getEncoder().encodeToString(picBytes);
	}

	@Before
	public void createNewFile() throws Exception {

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", ORIGINAL_DESCRIPTION);
		requestBody.put("title", ORIGINAL_TITLE);
		requestBody.put("data", ORIGINAL_BASE64_ENCODED_IMAGE);

		MvcResult resultActions = mvc.perform(post("/files").content(requestBody.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted()).andReturn();

		MockHttpServletResponse response = mvc.perform(asyncDispatch(resultActions)).andExpect(status().isCreated())
				.andExpect(header().exists("location")).andReturn().getResponse();

		this.resourceLocation = response.getHeader("location");

		Pattern pattern = Pattern.compile("(\\S+)/files/(\\d+)$");
		Matcher matcher = pattern.matcher(resourceLocation);
		assertTrue(matcher.matches());
		String serverUrl = matcher.group(1);
		int idFIle = Integer.valueOf(matcher.group(2));

		// the request to dowload the file dispatches an async operation on the server
		this.downloadEndpoint = serverUrl + "/download/" + idFIle;
	}

	@Test
	public void patchWithEmptyBodyMustNotAlterEntity() throws Exception {

		JSONObject requestBody = new JSONObject();

		// PATHC triggers a async operation on the backend
		var asyncResult = mvc.perform(
				patch(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		// when finished, returns status code is 204
		mvc.perform(asyncDispatch(asyncResult)).andExpect(status().isNoContent());


		mvc.perform(get(this.resourceLocation).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("description", is(ORIGINAL_DESCRIPTION)))
				.andExpect(jsonPath("title", is(ORIGINAL_TITLE))).andExpect(jsonPath("dateCreated").exists())
				.andExpect(jsonPath("data").doesNotExist());

		var asyncAction = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		mvc.perform(asyncDispatch(asyncAction)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(Base64.getDecoder().decode(ORIGINAL_BASE64_ENCODED_IMAGE)));
	}

	@Test
	public void pathcOnlyFileDescription() throws Exception {

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "new description");

		// PATHC triggers a async operation on the backend
		var asyncResult = mvc.perform(
				patch(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		// when finished, returns status code is 204
		mvc.perform(asyncDispatch(asyncResult)).andExpect(status().isNoContent());

		mvc.perform(get(this.resourceLocation).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("description", is("new description")))
				.andExpect(jsonPath("title", is(ORIGINAL_TITLE))).andExpect(jsonPath("dateCreated").exists())
				.andExpect(jsonPath("data").doesNotExist());

		var asyncAction = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		mvc.perform(asyncDispatch(asyncAction)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(Base64.getDecoder().decode(ORIGINAL_BASE64_ENCODED_IMAGE)));
	}

	@Test
	public void patchFileTitleAndDescription() throws Exception {

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "Description 123");
		requestBody.put("title", "title foo");

		// PATHC triggers a async operation on the backend
		var asyncResult = mvc.perform(
				patch(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		// when finished, returns status code is 204
		mvc.perform(asyncDispatch(asyncResult)).andExpect(status().isNoContent());

		mvc.perform(get(this.resourceLocation).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("description", is("Description 123"))).andExpect(jsonPath("title", is("title foo")))
				.andExpect(jsonPath("dateCreated").exists()).andExpect(jsonPath("data").doesNotExist());

		var asyncAction = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		mvc.perform(asyncDispatch(asyncAction)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(Base64.getDecoder().decode(ORIGINAL_BASE64_ENCODED_IMAGE)));

	}

	@Test
	public void putOnlyFileData() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("teatro-4-de-setembro.jpg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		String base64EncodedImage = Base64.getEncoder().encodeToString(picBytes);

		JSONObject requestBody = new JSONObject();
		requestBody.put("data", base64EncodedImage);

		var asyncResult = mvc.perform(
				patch(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		mvc.perform(asyncDispatch(asyncResult)).andExpect(status().isNoContent());

		mvc.perform(get(this.resourceLocation).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("description", is(ORIGINAL_DESCRIPTION)))
				.andExpect(jsonPath("title", is(ORIGINAL_TITLE)))
				.andExpect(jsonPath("dateCreated").exists())
				.andExpect(jsonPath("data").doesNotExist());

		var asyncAction = mvc.perform(get(downloadEndpoint)).andExpect(request().asyncStarted()).andReturn();

		mvc.perform(asyncDispatch(asyncAction)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(content().bytes(picBytes));

	}

}