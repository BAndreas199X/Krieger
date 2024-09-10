package com.andreas.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.dao.DocumentRepo;
import com.andreas.webapp.services.DocumentService;
import com.google.gson.Gson;

@RestController("document")
public class DocumentController {
	final static Gson globGson = new Gson();
	@Autowired
	DocumentRepo docRepo;
	@Autowired
	AuthorRepo authRepo;
	@Autowired
	DocumentService docService;
	
	/**
	 * An API endpoint used for creating a new document
	 * RequestPArameter: POST
	 * @param payload - a JSON String (won't accept any other String)
	 * @returns:
	 * - In case the document is successfully created and stored in the database, the created Document
	 *    is returned as ResponseEntity
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@PostMapping(path="/createDoc", consumes = "application/json")
	public ResponseEntity<Object> createDocument(@RequestBody String payload) {
		try {
			ResponseEntity<Object> createdDocument = docService.createDocumentService(payload);
			return createdDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/createDoc' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/createDoc': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint that can find and return a document corresponding the the provided Document_ID
	 * RequestPArameter: GET
	 * @param documentID
	 * @return - if a Document corresponding to the ID is found, it is returned via ReponseEntity
	 * 			- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@GetMapping("/getDocById")
	public ResponseEntity<Object> getDocumentById(@RequestParam("documentID") int documentID) {
		
		try {
			ResponseEntity<Object> foundDocument = docService.getDocumentByIdService(documentID);
			return foundDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/getDocById' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/getDocById': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint that will return ALL Documents present in the database
	 * RequestPArameter: GET
	 * @return - If successful, a List with all Documents present in the databse will be returned
	 * 			- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@GetMapping("/getAllDocs")
	public ResponseEntity<Object> getAllDocuments() {
		
		try {
			ResponseEntity<Object> allDocument = docService.getAllDocumentsService();
			return allDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/getAllDocs' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/getAllDocs': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint, that will delete a Document corresponding to the provided Document-ID
	 * RequestPArameter: DELETE
	 * @param documentID - ID of the document you want to delete
	 * @return - If successful, a ResponseEntity with a message confirming deletion is returned
	 * 			- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@DeleteMapping("/deleteDocByID")
	public ResponseEntity<Object> deleteDocumentByID(@RequestParam("documentID") int documentID){
		
		try {
			ResponseEntity<Object> deletedDocument = docService.deleteDocumentByIDService(documentID);
			return deletedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/deleteDocByID' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/deleteDocByID': "+e.toString());
		}
	}
	
	
	/**
	 * An API-endpoitn used to delete ALL documents present in the database
	 * RequestMEthod: DELETE
	 * @return - If successful, a ResponseEntity with a message confirming deletion is returned 
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@DeleteMapping("/deleteAllDocs")
	public ResponseEntity<Object> deleteAllDocuments(){
		
		try {
			ResponseEntity<Object> deletedDocument = docService.deleteAllDocumentsService();
			return deletedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/deleteAllDocs' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/deleteAllDocs': "+e.toString());
		}
	}
	
	
	/**
	 * An API-endpoint to update the title of a document corresponding to the provided ID
	 * RequestMethod: PUT
	 * @param documentID - ID of the document you want to update
	 * @param title - the title you want to update the document with
	 * @return - If successful, a ResponseEntity with the newly updated Document is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/updateDocumentTitle")
	public ResponseEntity<Object> updateDocumentTitle(@RequestParam("documentID") int documentID,
			@RequestParam("title") String title) {
		try {
			ResponseEntity<Object> updatedDocument = docService.updateDocumentTitleService(documentID,title);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/updateDocumentTitle' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/updateDocumentTitle': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint used to update an existing Document's Body
	 * RequestMethod: PUT
	 * @param payload  - a JSON String (won't accept any other String)
	 * @return - If successful, a ResponseMethod with the newly updated Document is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/updateDocumentBody", consumes = "application/json")
	public ResponseEntity<Object> updateDocumentBody(@RequestBody String payload){

		try {
			ResponseEntity<Object> updatedDocument = docService.updateDocumentBodyService(payload);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/updateDocumentBody' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/updateDocumentBody': "+e.toString());
		}
	}
	
	/**
	 * An API endpoint that can add an author as author to a document
	 * RequestMethod: PUT 
	 * @param documentID - ID of the document you want to add the author to
	 * @param authorID - The ID of the author you want to add as author to a document
	 * @return - if successful, a ResponseEntity with a message confirming the update is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/addAuthorToDocument")
	public ResponseEntity<Object> addAuthorToDocument(@RequestParam("documentID") int documentID,
			@RequestParam("authorID") int authorID) {
		
		try {
			ResponseEntity<Object> updatedDocument = docService.addAuthorToDocumentService(documentID,authorID);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/addAuthorToDocument' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/addAuthorToDocument': "+e.toString());
		}
	}
	
	/**
	 * An API endpoint that can remove an author as author from a document
	 * RequestMethod: PUT 
	 * @param documentID - ID of the document you want to remove the author from
	 * @param authorID - The ID of the author you want to remove as author from the document
	 * @return - if successful, a ResponseEntity with a message confirming the update is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/removeAuthorFromDocument")
	public ResponseEntity<Object> removeAuthorFromDocument(@RequestParam("documentID") int documentID,
			@RequestParam("authorID") int authorID) {
		
		try {
			ResponseEntity<Object> updatedDocument = docService.removeAuthorFromDocumentService(documentID,authorID);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/removeAuthorFromDocument' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/removeAuthorFromDocument': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint that can add a document as reference to a document
	 * RequestMethod: PUT 
	 * @param referencingID - ID of the document you want to add a new reference to
	 * @param referencedID - ID of the document you want to add as reference
	 * @return - if successful, a ResponseEntity with a message confirming the update is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/addReferenceToDocument")
	public ResponseEntity<Object> addReferenceToDocument(@RequestParam("referencingID") int referencingID,
			@RequestParam("referencedID") int referencedID) {
		
		try {
			ResponseEntity<Object> updatedDocument = docService.addReferenceToDocumentService(referencingID,referencedID);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/addReferenceToDocument' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/addReferenceToDocument': "+e.toString());
		}
	}
	
	
	/**
	 * An API endpoint that can remove a document as reference from a document
	 * RequestMethod: PUT 
	 * @param referencingID - ID of the document you want to remove a new reference from
	 * @param referencedID - ID of the document you want to remove as reference
	 * @return - if successful, a ResponseEntity with a message confirming the update is returned
	 * 	  		- In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    		ErrorMessage or another remark is returned
	 */
	@PutMapping(path="/removeReferenceFromDocument")
	public ResponseEntity<Object> removeReferenceFromDocument(@RequestParam("referencingID") int referencingID,
			@RequestParam("referencedID") int referencedID) {
		
		try {
			ResponseEntity<Object> updatedDocument = docService.removeReferenceFromDocumentService(referencingID,referencedID);
			return updatedDocument;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'document/removeReferenceFromDocument' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'document/removeReferenceFromDocument': "+e.toString());
		}
	}
}