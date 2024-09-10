package com.andreas.webapp.controllers;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.dao.DocumentRepo;
import com.andreas.webapp.model.Author;
import com.andreas.webapp.services.AuthorService;
import com.andreas.webapp.services.DocumentService;

@WebMvcTest
class AuthorControllerTest {

	final static int MOCK_ID = 1;
	final static Author MOCK_AUTHOR1 = new Author(100,"Joe","Generic");
	final static Author MOCK_AUTHOR2 = new Author(101,"Max","Musterman");
	final static List<Author> MOCK_AUTHORS = Arrays.asList(MOCK_AUTHOR1,MOCK_AUTHOR2);
	final static ResponseEntity<Object> ASPIRING_RESULT = new ResponseEntity<>(MOCK_AUTHOR1,HttpStatus.OK);
	final static ResponseEntity<Object> RESULT_LIST = new ResponseEntity<>(MOCK_AUTHORS,HttpStatus.OK);
	
	@MockBean
    AuthorService authService;
	
	@MockBean
    DocumentService docService;
	
	@MockBean
    DocumentRepo docRepo;
	
	@MockBean
    AuthorRepo authRepo;
	

	@Autowired
    private MockMvc mockMvc;
	
	@InjectMocks
    AuthorController authController;
	
	@Test
	void testCreateAuthor() throws Exception {
		
		
		Mockito.when(this.authService.createAuthorService(any(Author.class))).thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/createAuth").param("authorID", String.valueOf(MOCK_ID))
				.param("firstName", MOCK_AUTHOR1.getFirstName()).param("lastName", MOCK_AUTHOR1.getLastName()))
		.andExpect(status().isOk());//.andReturn();
		//andDo(print()).
	}
	
	
	@Test
	void testGetAuthorById() throws Exception {
		
		Mockito.when(this.authService.getAuthorByIdService(MOCK_ID)).thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/getAuthById").param("authorID", String.valueOf(MOCK_ID)))
		.andExpect(status().isOk());
	}
	
	
	@Test
	void testGetAllAuthors() throws Exception {
		
		Mockito.when(this.authService.getAllAuthorsService()).thenReturn(RESULT_LIST);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/getAllAuth")).andExpect(status().isOk());
		
	}

	
	@Test
	void testGetByNames() throws Exception {
		Mockito.when(this.authService.getByNamesService("Joe","Generic")).thenReturn(RESULT_LIST);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/getAuthByNames")
				.param("firstName", MOCK_AUTHOR1.getFirstName()).param("lastName", MOCK_AUTHOR1.getLastName()))
		.andExpect(status().isOk());
	}

	@Test
	void testDeleteAuthorById() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Author successfully deleted!");
		
		Mockito.when(this.authService.deleteAuthorByIdService(MOCK_ID)).thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAuthByID")
				.param("authorID", String.valueOf(MOCK_ID))).andExpect(status().isOk());
		
	}

	@Test
	void testDeleteAll() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"All Authors successfully deleted!");
		
		Mockito.when(this.authService.deleteAllService()).thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAllAuth"))
                .andExpect(status().isOk());//.andReturn();
	}

	@Test
	void testUpdateFirstName() throws Exception {
		Mockito.when(this.authService.updateFirstNameService(MOCK_ID, MOCK_AUTHOR1.getFirstName()))
		.thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/createAuth").param("authorID", String.valueOf(MOCK_ID))
				.param("firstName", MOCK_AUTHOR1.getFirstName()))
		.andExpect(status().isOk());
	}
	
	@Test
	void testUpdateLastName() throws Exception {
		Mockito.when(this.authService.updateLastNameService(MOCK_ID, MOCK_AUTHOR1.getLastName()))
		.thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/createAuth").param("authorID", String.valueOf(MOCK_ID))
				.param("lastName", MOCK_AUTHOR1.getLastName()))
		.andExpect(status().isOk());
	}

}
