package com.personal.page.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.personal.page.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAccountControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void verifyAllToDoList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON));
		
	}
	
	@Test
	public void verifyUserAccountById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/3").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.active").exists())
		.andExpect(jsonPath("$.dateOfBirth").exists())
		.andExpect(jsonPath("$.email").exists())
		.andExpect(jsonPath("$.enabled").exists())
		.andExpect(jsonPath("$.firstName").exists())
		.andExpect(jsonPath("$.lastName").exists())
		.andExpect(jsonPath("$.password").exists())
		.andExpect(jsonPath("$.username").exists())
		.andDo(print());
	}
	
	@Test
	public void verifyInvalidUserAccountArgument()  throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user/u").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(400))
		.andExpect(jsonPath("$.message").value("Invalid request: the server recieved malformed syntax."))
		.andDo(print());
	}
	
	@Test
	public void verifyInvalidUserAccountId()  throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user/0").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.message").value("No usser account found by this id: 0" ))
		.andDo(print());
	}
	
	@Test
	public void verifyNullUserAccount()  throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user/110").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(404))
		.andExpect(jsonPath("$.message").value("No usser account found by this id: 110" ))
		.andDo(print());
	}
	
	
	@Test
	public void verifyInvalidUserAccountDeleteById()  throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAccount/110").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errorCode").value(400))
		.andDo(print());
	}
	
	
	
	

}
