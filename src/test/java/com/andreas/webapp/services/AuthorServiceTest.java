package com.andreas.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.model.Author;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

	final static Author MOCK_AUTHOR1 = new Author(100,"Joe","Generic");
	final static Author MOCK_AUTHOR2 = new Author(101,"Max","Musterman");
	final static List<Author> MOCK_AUTHORS = Arrays.asList(MOCK_AUTHOR1,MOCK_AUTHOR2);
	final static int MOCK_ID = 1;
	@Mock
    private AuthorRepo authRepo;

    @InjectMocks
    private AuthorService authService;
	@Test
	void testCreateAuthorService() throws Exception {
		
		Mockito.when(this.authRepo.save(Mockito.any(Author.class)))
        .thenReturn(new Author(1,"Joe","Generic"));
		
		ResponseEntity<Object> result = authService.createAuthorService(
				MOCK_AUTHOR1);
		Author a = (Author) result.getBody();
		assertEquals(a.getClass(), Author.class);
		assertEquals(a.getAuthorID(), 1);
		assertEquals(a.getFirstName(),"Joe");
		assertEquals(a.getLastName(),"Generic");
	}

	@Test
	void testGetAuthorByIdService() throws Exception {
		
		Mockito.when(this.authRepo.findById(MOCK_ID)).thenReturn(
				Optional.of(new Author(1,"Joe","Generic")));
		
		ResponseEntity<Object> result = authService.getAuthorByIdService(MOCK_ID);
		Author a = (Author) result.getBody();
		assertEquals(a.getClass(), Author.class);
		assertEquals(a.getAuthorID(), 1);
		assertEquals(a.getFirstName(),"Joe");
		assertEquals(a.getLastName(),"Generic");
	}
	
	
	@Test
	void testGetAllAuthorsService() throws Exception {
		Mockito.when(this.authRepo.findAll()).thenReturn(MOCK_AUTHORS);
		
		ResponseEntity<Object> result = authService.getAllAuthorsService();
		List<Author> a = (List<Author>) result.getBody();
		
		assertTrue(a.size()==2);
		assertTrue(a.get(0).getAuthorID()==100);
		assertTrue(a.get(1).getAuthorID()==101);
	}
	
	
	@Test
	void testGetByNamesService() throws Exception {
		
		Mockito.when(this.authRepo.findByNames("abc", "xyz")).
		thenReturn(MOCK_AUTHORS);
		
		ResponseEntity<Object> result = authService.getByNamesService("abc", "xyz");
		List<Author> a = (List<Author>) result.getBody();
		
		assertTrue(a.size()==2);
		assertTrue(a.get(0).getAuthorID()==100);
		assertTrue(a.get(1).getAuthorID()==101);
	}

	
	@Test
	void testDeleteAuthorByIdService() throws Exception {
		Mockito.when(authRepo.existsById(MOCK_ID)).thenReturn(true);
		
		ResponseEntity<Object> result = authService.deleteAuthorByIdService(MOCK_ID);
		String s = (String) result.getBody();
		assertEquals(s,"Author successfully deleted!");
	}
	
	@Test
	void testDeleteAuthorByIdServiceFalse() throws Exception {
		Mockito.when(authRepo.existsById(MOCK_ID)).thenReturn(false);
		
		ResponseEntity<Object> result = authService.deleteAuthorByIdService(MOCK_ID);
		String s = (String) result.getBody();
		assertEquals(s,"No Author found for the provided Author-ID!");
	}

	
	@Test
	void testDeleteAllService() throws Exception {
		ResponseEntity<Object> result = authService.deleteAllService();
		String s = (String) result.getBody();
		assertEquals(s,"All Authors successfully deleted!");
	}

	@Test
	void testUpdateFirstNameService() throws Exception {
		
		Mockito.when(this.authRepo.findById(MOCK_ID)).thenReturn(
				Optional.of(new Author(1,"Joe","Generic")));
		
		Mockito.when(this.authRepo.save(Mockito.any(Author.class)))
        .thenReturn(new Author(1,"Eoj","Generic"));
		
		ResponseEntity<Object> result = authService.updateFirstNameService(MOCK_ID, "Eoj");
		
		Author a = (Author) result.getBody();
		
		assertEquals(a.getClass(), Author.class);
		assertEquals(a.getAuthorID(), 1);
		assertEquals(a.getFirstName(),"Eoj");
		assertFalse(a.getFirstName().equals("Joe"));
	}

	@Test
	void testUpdateLastNameService() throws Exception {
		Mockito.when(this.authRepo.findById(MOCK_ID)).thenReturn(
				Optional.of(new Author(1,"Joe","Generic")));
		
		Mockito.when(this.authRepo.save(Mockito.any(Author.class)))
        .thenReturn(new Author(1,"Joe","Cireneg"));
		
		ResponseEntity<Object> result = authService.updateLastNameService(MOCK_ID, "Eoj");
		
		Author a = (Author) result.getBody();
		
		assertEquals(a.getClass(), Author.class);
		assertEquals(a.getAuthorID(), 1);
		assertEquals(a.getLastName(),"Cireneg");
		assertFalse(a.getLastName().equals("Generic"));
	}

}
