package com.andreas.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.dao.DocumentRepo;
import com.andreas.webapp.model.Author;
import com.andreas.webapp.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
	
	@Mock
    private DocumentRepo docRepo;
	@Mock
    private AuthorRepo authRepo;

    @InjectMocks
    private DocumentService docService;
    
    static String MOCK_PAYLOAD;
	final static Author MOCK_AUTHOR1 = new Author(1,"Joe","Generic");
	final static Author MOCK_AUTHOR2 = new Author(2,"Max","Musterman");
	final static List<Author> MOCK_AUTHORS = Arrays.asList(MOCK_AUTHOR1,MOCK_AUTHOR2);
	final static Set<Author> AUTHORS_SET = new HashSet<>(MOCK_AUTHORS);
	final static Document MOCK_DOCUMENT_BASIC = new Document(1,"Mock title","Mock Body");
	final static Document MOCK_DOCUMENT_SPARE = new Document(2,"Spare title","Spare Body");
	final static Document MOCK_DOCUMENT_FULL = new Document(3,"Generic title","Generic Body");
	static List<Document> MOCK_DOCUMENTS;
	
	static ResponseEntity<Object> ASPIRING_RESULT;
	final static List<Document> DOCUMENT_LIST = new ArrayList<>();
	static ResponseEntity<Object> RESULT_LIST;
	final static Map<String,Object> PAYLOAD_MAP = new HashMap<>();
	final static Set<Integer> AUTHOR_SET = new HashSet<>();
	final static Set<Integer> REFERENCE_SET = new HashSet<>();
	
	final static int MOCK_ID = 3;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		AUTHOR_SET.add(1);
		AUTHOR_SET.add(2);
		
		REFERENCE_SET.add(1);
		
		PAYLOAD_MAP.put("documentID", 3);
		PAYLOAD_MAP.put("documentTitle", "Generic title");
		PAYLOAD_MAP.put("documentBody", "Generic Body");
		PAYLOAD_MAP.put("authors", AUTHOR_SET);
		PAYLOAD_MAP.put("references", REFERENCE_SET);
		
		MOCK_PAYLOAD = asJsonString(PAYLOAD_MAP);
		
		
		Set<Document> references = new HashSet<>();
		references.add(MOCK_DOCUMENT_BASIC);
		MOCK_DOCUMENT_FULL.setReferences(references);
		MOCK_DOCUMENT_FULL.setAuthors(new HashSet<>(MOCK_AUTHORS));
		
		ASPIRING_RESULT = new ResponseEntity<>(MOCK_DOCUMENT_FULL,HttpStatus.OK);
		
		DOCUMENT_LIST.add(MOCK_DOCUMENT_BASIC);
		DOCUMENT_LIST.add(MOCK_DOCUMENT_SPARE);
		DOCUMENT_LIST.add(MOCK_DOCUMENT_FULL);
		RESULT_LIST = new ResponseEntity<>(DOCUMENT_LIST,HttpStatus.OK);
		
		MOCK_DOCUMENTS = Arrays.asList(
				MOCK_DOCUMENT_BASIC,MOCK_DOCUMENT_SPARE,MOCK_DOCUMENT_FULL);
	}
	
	@Test
	void testCreateDocumentService() throws Exception {
		Document testDocument = new Document(3,"Generic title","Generic Body");
		Mockito.when(this.docRepo.save(any(Document.class))).thenReturn(testDocument);
		Mockito.when(this.docRepo.existsById(any(Integer.class))).thenReturn(true);
		Mockito.when(this.authRepo.existsById(any(Integer.class))).thenReturn(true);
		
		Mockito.when(docRepo.findById(1)).thenReturn(Optional.of(MOCK_DOCUMENT_BASIC));
		
		Mockito.when(authRepo.findById(1)).thenReturn(Optional.of(MOCK_AUTHOR1));
		Mockito.when(authRepo.findById(2)).thenReturn(Optional.of(MOCK_AUTHOR2));
		
		
		ResponseEntity<Object> result = docService.createDocumentService(MOCK_PAYLOAD);
		
		
		Document d = (Document) result.getBody();
		
		assertEquals(d,MOCK_DOCUMENT_FULL);
	}
	
	@Test
	void testCreateDocumentServiceAllErrorScenarios() throws Exception {
		
		//checks for missing document-ID
		PAYLOAD_MAP.remove("documentID");
		ResponseEntity<Object> result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"DocumentID, Document title, and document body must be "
				+ "included in the payload!");
		
		//checks for missing document-Title
		PAYLOAD_MAP.remove("documentTitle");
		PAYLOAD_MAP.put("documentID", 3);
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"DocumentID, Document title, and document body must be "
				+ "included in the payload!");
		
		//checks for missing document-Body
		PAYLOAD_MAP.remove("documentBody");
		PAYLOAD_MAP.put("documentTitle", "Generic title");
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"DocumentID, Document title, and document body must be "
				+ "included in the payload!");
		
		//checks for missing author-List
		PAYLOAD_MAP.put("documentBody", "Generic Body");
		PAYLOAD_MAP.remove("authors");
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"A list with at least one author-ID must be "
				+ "included in the payload!");
		
		//checks for empty author-List
		PAYLOAD_MAP.put("authors", new HashSet<>());
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"The author list must not be empty! Please make sure at"
				+ " least one valid author-ID included in the payload!");
		
		//invalid Author-ID in author-list
		Mockito.when(this.authRepo.existsById(any(Integer.class))).thenReturn(false);
		PAYLOAD_MAP.put("authors", AUTHOR_SET);
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"At least one author listed is not in the system! Please "
				+ "make sure all listed authored have been registered in the system!");

		//invalid Document-ID in reference-list
		Mockito.when(this.docRepo.existsById(any(Integer.class))).thenReturn(false);
		Mockito.when(this.authRepo.existsById(any(Integer.class))).thenReturn(true);
		result = docService.createDocumentService(asJsonString(PAYLOAD_MAP));
		assertEquals(result.getBody().toString(),"At least one document listed as reference is not in the system! "
				+ "Please make sure all listed documents have been registered in the system!");
	}

	@Test
	void testGetDocumentByIdService() throws Exception {
		Mockito.when(this.docRepo.findById(MOCK_ID)).thenReturn(Optional.of(
				new Document(3,"Generic title","Generic Body")));
		Mockito.when(docRepo.findById(1)).thenReturn(Optional.of(MOCK_DOCUMENT_BASIC));
		
		Mockito.when(authRepo.findById(1)).thenReturn(Optional.of(MOCK_AUTHOR1));
		Mockito.when(authRepo.findById(2)).thenReturn(Optional.of(MOCK_AUTHOR2));
		
		Mockito.when(docRepo.findAuthorsOfDocument(MOCK_ID)).thenReturn(AUTHOR_SET);
		Mockito.when(docRepo.findReferencesOfDocument(MOCK_ID)).thenReturn(REFERENCE_SET);	
		
		ResponseEntity<Object> result = this.docService.getDocumentByIdService(MOCK_ID);
		
		Document d = (Document) result.getBody();
		assertEquals(d,MOCK_DOCUMENT_FULL);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAllDocumentsService() throws Exception {
		Mockito.when(this.docRepo.findAll()).thenReturn(DOCUMENT_LIST);
		
		Mockito.when(docRepo.findAuthorsOfDocument(any(Integer.class))).thenReturn(AUTHOR_SET);
		Mockito.when(authRepo.findById(1)).thenReturn(Optional.of(MOCK_AUTHOR1));
		Mockito.when(authRepo.findById(2)).thenReturn(Optional.of(MOCK_AUTHOR2));
		
		Mockito.when(docRepo.findReferencesOfDocument(any(Integer.class))).thenReturn(REFERENCE_SET);
		Mockito.when(docRepo.findById(1)).thenReturn(Optional.of(MOCK_DOCUMENT_BASIC));
		
		ResponseEntity<Object> result = this.docService.getAllDocumentsService();
		List<Document> docList = (List<Document>) result.getBody();
		
		assertEquals(3,docList.size());
	}

	
	@Test
	void testDeleteDocumentByIDService() throws Exception {
		Mockito.when(docRepo.existsById(MOCK_ID)).thenReturn(true);
		
		ResponseEntity<Object> result = docService.deleteDocumentByIDService(MOCK_ID);
		String s = (String) result.getBody();
		assertEquals(s,"Document successfully deleted!");
	}
	
	@Test
	void testDeleteDocumentByIDServiceFalse() throws Exception {
		Mockito.when(docRepo.existsById(MOCK_ID)).thenReturn(false);
		
		ResponseEntity<Object> result = docService.deleteDocumentByIDService(MOCK_ID);
		String s = (String) result.getBody();
		assertEquals(s,"Document with that ID does not exist and can therefore not be deleted!");
	}

	@Test
	void testDeleteAllDocumentsService() throws Exception {
		ResponseEntity<Object> result = docService.deleteAllDocumentsService();
		String s = (String) result.getBody();
		assertEquals(s,"All Documents successfully deleted!");
	}

	
	@Test
	void testUpdateDocumentTitleService() throws Exception {
		Mockito.when(this.docRepo.findById(2)).thenReturn(Optional.of(MOCK_DOCUMENT_SPARE));

		Document newDocument = new Document(2,"Updated title","Spare Body");
		Mockito.when(this.docRepo.save(any(Document.class))).thenReturn(newDocument);
		
		ResponseEntity<Object> result = docService.updateDocumentTitleService(2, "Updated title");
		
		Document resultDoc = (Document) result.getBody();
		assertEquals(newDocument,resultDoc);
		assertEquals("Updated title",resultDoc.getDocumentTitle());		
	}

	
	@Test
	void testUpdateDocumentBodyService() throws Exception {
		
		Map<String,Object> newPayload = new HashMap<>();
		newPayload.put("documentID", 2);
		newPayload.put("documentBody", "Updated Body");
		
		Document newDocument = new Document(2,"Spare title","Updated Body");
		
		Mockito.when(this.docRepo.findById(2)).thenReturn(Optional.of(MOCK_DOCUMENT_SPARE));
		Mockito.when(this.docRepo.save(any(Document.class))).thenReturn(newDocument);
		
		ResponseEntity<Object> result = docService.updateDocumentBodyService(asJsonString(newPayload));
		
		Document d = (Document) result.getBody();
		
		assertEquals(d,newDocument);
	}

	
	@Test
	void testAddAuthorToDocumentService() throws Exception {
		Mockito.when(docRepo.findAuthorsOfDocument(2)).thenReturn(new HashSet<>());
		ResponseEntity<Object> result = docService.addAuthorToDocumentService(2, 1);
		
		String s = (String) result.getBody();
		
		assertEquals(s,"Author successfully added as author to document!");
		
	}
	
	
	@Test
	void testRemoveAuthorFromDocumentService() throws Exception {
		Mockito.when(docRepo.findAuthorsOfDocument(2)).thenReturn(AUTHOR_SET);
		
		ResponseEntity<Object> result = docService.removeAuthorFromDocumentService(2, 1);
		
		String s = (String) result.getBody();
		
		assertEquals(s,"Author successfully removed as author from Document!");
	}

	
	@Test
	void testAddReferenceToDocumentService() throws Exception {
		Mockito.when(docRepo.findReferencesOfDocument(2)).thenReturn(new HashSet<>());
		
		ResponseEntity<Object> result = docService.addReferenceToDocumentService(2, 1);
		
		String s = (String) result.getBody();
		
		assertEquals(s,"Document successfully added as reference!");
		
	}

	
	@Test
	void testRemoveReferenceFromDocumentService() throws Exception {
		Mockito.when(docRepo.findReferencesOfDocument(2)).thenReturn(REFERENCE_SET);
		ResponseEntity<Object> result = docService.removeReferenceFromDocumentService(2, 1);
		
		String s = (String) result.getBody();
		
		assertEquals(s,"Document successfully removed as reference!");
	}
	
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
