package com.andreas.webapp.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.dao.DocumentRepo;
import com.andreas.webapp.model.Author;
import com.andreas.webapp.model.Document;
import com.andreas.webapp.services.AuthorService;
import com.andreas.webapp.services.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class DocumentControllerTest {

	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private DocumentService docService;
	@MockBean
	private AuthorService authService;
	@MockBean
	private DocumentRepo docRepo;
	@MockBean
	private AuthorRepo authRepo;
	
	static String MOCK_PAYLOAD;
	final static Author MOCK_AUTHOR1 = new Author(100,"Joe","Generic");
	final static Author MOCK_AUTHOR2 = new Author(101,"Max","Musterman");
	final static List<Author> MOCK_AUTHORS = Arrays.asList(MOCK_AUTHOR1,MOCK_AUTHOR2);
	final static Set<Author> AUTHORS_SET = new HashSet<>(MOCK_AUTHORS);
	final static Document MOCK_DOCUMENT_BASIC = new Document(1,"Mock title","Mock Body");
	final static Document MOCK_DOCUMENT_SPARE = new Document(2,"Spare title","SPare Body");
	final static Document MOCK_DOCUMENT_FULL = new Document(3,"Generic title","Generic Body");
	static ResponseEntity<Object> ASPIRING_RESULT;
	final static List<Document> DOCUMENT_LIST = new ArrayList<>();
	static ResponseEntity<Object> RESULT_LIST;
	
	final static int MOCK_ID = 1;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MOCK_DOCUMENT_FULL.setAuthors(AUTHORS_SET);
		Set<Document> references = new HashSet<>();
		references.add(MOCK_DOCUMENT_BASIC);
		MOCK_DOCUMENT_FULL.setReferences(references);
		
		ASPIRING_RESULT = new ResponseEntity<>(MOCK_DOCUMENT_FULL,HttpStatus.OK);
		
		DOCUMENT_LIST.add(MOCK_DOCUMENT_BASIC);
		DOCUMENT_LIST.add(MOCK_DOCUMENT_FULL);
		RESULT_LIST = new ResponseEntity<>(DOCUMENT_LIST,HttpStatus.OK);
		
		MOCK_PAYLOAD = asJsonString(MOCK_DOCUMENT_FULL);
	}
	
	@Test
	void testCreateDocument() throws Exception {
		Mockito.when(this.docService.createDocumentService(MOCK_PAYLOAD)).thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform( MockMvcRequestBuilders.post("/createDoc")
				.content(MOCK_PAYLOAD).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testGetDocumentById() throws Exception {
		Mockito.when(this.docService.getDocumentByIdService(MOCK_ID)).thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/getDocById").param("documentID", String.valueOf(MOCK_ID)))
		.andExpect(status().isOk());
	}

	@Test
	void testGetAllDocuments() throws Exception {
		Mockito.when(this.docService.getAllDocumentsService()).thenReturn(RESULT_LIST);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/getAllDocs"))
		.andExpect(status().isOk());
	}

	
	@Test
	void testDeleteDocumentByID() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Document successfully deleted!");
		
		Mockito.when(this.docService.deleteDocumentByIDService(MOCK_ID)).thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteDocByID")
				.param("documentID", String.valueOf(MOCK_ID))).andExpect(status().isOk());
	}

	
	@Test
	void testDeleteAllDocuments() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"All Documents successfully deleted!");
		
		Mockito.when(this.docService.deleteAllDocumentsService()).thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAllDocs"))
                .andExpect(status().isOk());
	}

	
	@Test
	void testUpdateDocumentTitle() throws Exception {
		Mockito.when(this.docService.updateDocumentTitleService(MOCK_ID, "Generic title")).
		thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/updateDocumentTitle")
				.param("documentID", String.valueOf(MOCK_ID)).param("title", "Generic title"))
		.andExpect(status().isOk());
	}

	
	@Test
	void testUpdateDocumentBody() throws Exception {
		Document updateDocument = new Document(1,null,"Updated Body");
		System.out.println(asJsonString(updateDocument));
		Mockito.when(this.docService.updateDocumentBodyService(asJsonString(updateDocument))).
			thenReturn(ASPIRING_RESULT);
		
		mockMvc.perform( MockMvcRequestBuilders.put("/updateDocumentBody")
				.content(asJsonString(updateDocument)).contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	
	@Test
	void testAddAuthorToDocument() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Author successfully added as author to document!");
		
		Mockito.when(this.docService.addAuthorToDocumentService(MOCK_ID, MOCK_AUTHOR2.getAuthorID())).
		thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/addAuthorToDocument")
				.param("documentID", String.valueOf(MOCK_ID)).param("authorID", String.valueOf(
						MOCK_AUTHOR2.getAuthorID()))).andExpect(status().isOk());
	}

	
	@Test
	void testRemoveAuthorFromDocument() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Author successfully removed as author from Document!");
		
		Mockito.when(this.docService.removeAuthorFromDocumentService(MOCK_ID, MOCK_AUTHOR1.getAuthorID())).
		thenReturn(asprResult);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/removeAuthorFromDocument")
				.param("documentID", String.valueOf(MOCK_ID)).param("authorID", String.valueOf(
						MOCK_AUTHOR1.getAuthorID()))).andExpect(status().isOk());
	}

	
	@Test
	void testAddReferenceToDocument() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Document successfully added as reference!");
		
		Mockito.when(this.docService.addReferenceToDocumentService(MOCK_ID, MOCK_DOCUMENT_SPARE.getDocumentID())).
		thenReturn(asprResult);
		
		
		mockMvc.perform(MockMvcRequestBuilders.put("/addReferenceToDocument")
				.param("referencingID", String.valueOf(MOCK_ID)).param("referencedID", String.valueOf(
						MOCK_DOCUMENT_SPARE.getDocumentID()))).andExpect(status().isOk());
	}

	
	@Test
	void testRemoveReferenceFromDocument() throws Exception {
		ResponseEntity<Object> asprResult = ResponseEntity.status(HttpStatus.OK).body(
				"Document successfully removed as reference!");
		
		Mockito.when(this.docService.removeReferenceFromDocumentService(MOCK_ID, MOCK_DOCUMENT_BASIC.getDocumentID())).
		thenReturn(asprResult);
		
		
		mockMvc.perform(MockMvcRequestBuilders.put("/removeReferenceFromDocument")
				.param("referencingID", String.valueOf(MOCK_ID)).param("referencedID", String.valueOf(
						MOCK_DOCUMENT_BASIC.getDocumentID()))).andExpect(status().isOk());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}


