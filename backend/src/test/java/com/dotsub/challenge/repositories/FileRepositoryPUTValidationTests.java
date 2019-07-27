package com.dotsub.challenge.repositories;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import javax.validation.ConstraintViolationException;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
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
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class FileRepositoryPUTValidationTests {


	@Autowired
	private MockMvc mvc;
	private String resourceLocation;

	@Before
	public void createNewFile() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cable-stayed-bridge.jpeg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		String base64EncodedImage = Base64.getEncoder().encodeToString(picBytes);

		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "wrong description");
		requestBody.put("title", "wrong title");
		requestBody.put("data", base64EncodedImage);

		MvcResult resultActions = mvc.perform(post("/files").content(requestBody.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted()).andReturn();

		MockHttpServletResponse response = mvc.perform(asyncDispatch(resultActions)).andExpect(status().isCreated())
				.andExpect(header().exists("location")).andReturn().getResponse();

		this.resourceLocation = response.getHeader("location");		
	}

	@Test
	public void putEmptyBodyMustFail() throws Exception {

		JSONObject requestBody = new JSONObject();

		try {
			mvc.perform(put(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON));
			fail();
		} catch (NestedServletException e) {
			assertThat(e.getCause() , Matchers.instanceOf(ConstraintViolationException.class));
		}
    }
    
    @Test
	public void putOnlyFileDescriptionMustFail() throws Exception {

        JSONObject requestBody = new JSONObject();
        requestBody.put("description", "Description 123");

		try {
			mvc.perform(put(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON));
			fail();
		} catch (NestedServletException e) {
			assertThat(e.getCause() , Matchers.instanceOf(ConstraintViolationException.class));
		}
    }
    
    @Test
	public void putOnlyFileTitleAndDescriptionMustFail() throws Exception {

        JSONObject requestBody = new JSONObject();
        requestBody.put("description", "Description 123");
        requestBody.put("title", "title foo");

		try {
			mvc.perform(put(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON));
			fail();
		} catch (NestedServletException e) {
			assertThat(e.getCause() , Matchers.instanceOf(ConstraintViolationException.class));
		}
    }    
    

    @Test
	public void putOnlyFileDataMustFail() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cable-stayed-bridge.jpeg").getFile());

		byte[] picBytes = Files.readAllBytes(file.toPath());
		String base64EncodedImage = Base64.getEncoder().encodeToString(picBytes);


        JSONObject requestBody = new JSONObject();
        requestBody.put("data", base64EncodedImage);

		try {
			mvc.perform(put(this.resourceLocation).content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON));
			fail();
		} catch (NestedServletException e) {
			assertThat(e.getCause() , Matchers.instanceOf(ConstraintViolationException.class));
		}
    }       

    
}