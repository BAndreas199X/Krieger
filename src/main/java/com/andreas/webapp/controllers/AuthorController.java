package com.andreas.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.andreas.webapp.model.Author;
import com.andreas.webapp.services.AuthorService;

@RestController("author")
public class AuthorController {
	
	@Autowired
	AuthorService authService;
	
	/**
	 * API endpoint that can be used to create a new author
	 * RequestMethod: POST
	 * @param athr: an Object of type "Author" it can be imitated by giving AuthorID, FirstName, 
	 * and LastName as parameters of a query
	 * @returns:
	 * - In case the author is successfully created and stored in the database, the created Author
	 *    is returned as ResponseEntity
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@PostMapping("/createAuth")
	@ResponseBody
	public ResponseEntity<Object> createAuthor(Author athr) {
		
		try {
			ResponseEntity<Object> newAuthor = authService.createAuthorService(athr);
			return newAuthor;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/create' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/create': "+e.toString());
		}
		
	}
	
	
	/**
	 * API endpoint that is used to retrieve an existing author
	 * RequestMEthod: GET
	 * @param authorID - The ID of the author we're searching for
	 * @return
	 * - In case the author corresponding to the ID is found, he is returned via ReponseEntity
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@GetMapping("/getAuthById")
	@ResponseBody
	public ResponseEntity<Object> getAuthorById(@RequestParam("authorID") int authorID) {
		
		try {
			ResponseEntity<Object> foundAuthor = 
					authService.getAuthorByIdService(authorID);
			return foundAuthor;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/getById' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/getById': "+e.toString());
		}
		
	}
	
	/**
	 * An API endpoint used to retrieve ALL existing authors
	 * RequestMethod: GET
	 * @return
	 * - If nothing unforseen happens, a ResponseEntity with a List of all the authors is returned
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@GetMapping("/getAllAuth")
	@ResponseBody
	public ResponseEntity<Object> getAllAuthors() {
		
		try {
			ResponseEntity<Object> allAuthors = authService.getAllAuthorsService();
			return allAuthors;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/getAll' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/getAll': "+e.toString());
		}
	}
	
	/**
	 * API endpoint that can find an author based on first and last name, 
	 * first and last name don't need to be completed, it's sufficient if a part corresponds
	 * RequestMethod: GET
	 * 
	 * @param firstName - (partial) first name of the author you are searching for
	 * @param lastName - (partial) last name of the author you are searching for
	 * @return
	 * - A ResponseEntity with a List of all the authors corresponding to the input is returned
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@GetMapping("/getAuthByNames")
	@ResponseBody
	public ResponseEntity<Object> getByNames(@RequestParam("firstName") String firstName, 
			@RequestParam("lastName") String lastName) {
		
		try {
			ResponseEntity<Object> matchingAuthors = authService.getByNamesService(firstName, lastName);
			return matchingAuthors;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/getByNames' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/getByNames': "+e.toString());
		}
	}
	
	/**
	 * An API endpoint to delete the author corresponding to the provided ID
	 * RequestMethod: DELETE
	 * 
	 * @param authorID - ID of the author that is to be deleted
	 * @return
	 * - If successful, the ResponseEntity contains a message confirming the deletion
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@DeleteMapping("/deleteAuthByID")
	@ResponseBody
	public ResponseEntity<Object> deleteAuthorById(@RequestParam("authorID") int authorID) {
		try{
			ResponseEntity<Object> deletionSuccessful = authService.deleteAuthorByIdService(authorID);
			return deletionSuccessful;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/deleteByID' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/deleteByID': "+e.toString());
		}
	}
	
	/**
	 * An API endpoint to delete all authors in the database
	 * RequestMethod: DELETE
	 * 
	 * @return
	 * - If successful, the ResponseEntity contains a message confirming the deletion
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@DeleteMapping("/deleteAllAuth")
	@ResponseBody
	public ResponseEntity<Object> deleteAll() {
		
		try{
			ResponseEntity<Object> deletionSuccessful = authService.deleteAllService();
			return deletionSuccessful;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/deleteAll' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/deleteAll': "+e.toString());
		}	
	}
	
	/**
	 * An API endpoint used to update an author's first name
	 * RequestMethod: PUT
	 * @param authorID - ID of the author that is to be updated
	 * @param firstName - the first name the author should be updated to
	 * 
	 * 
	 * @return
	 * - If successful, the ResponseEntity contains the updated Author-object
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@PutMapping("/changeAuthorFirstName")
	@ResponseBody
	public ResponseEntity<Object> updateFirstName(@RequestParam("authorID") int authorID,
			@RequestParam("firstName") String firstName) {
		
		try {
			ResponseEntity<Object> updateSuccessful = 
					authService.updateFirstNameService(authorID, firstName);
			
			return updateSuccessful;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/changeAuthorFirstName' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/changeAuthorFirstName': "+e.toString());
		}	
	}
	
	/**
	 * An API endpoint used to update an author's last name
	 * RequestMethod: PUT
	 * @param authorID - ID of the author that is to be updated
	 * @param lastName - the last name the author should be updated to
	 * 
	 * 
	 * @return
	 * - If successful, the ResponseEntity contains the updated Author-object
	 * - In case there is an error, or another undesired output, a ResponseEntity with the 
	 *    ErrorMessage or another remark is returned
	 */
	@PutMapping("/changeAuthorLastName")
	@ResponseBody
	public ResponseEntity<Object> updateLastName(@RequestParam("authorID") int authorID,
			@RequestParam("lastName") String lastName) {
		try {
			ResponseEntity<Object> updateSuccessful = 
					authService.updateLastNameService(authorID, lastName);
			
			return updateSuccessful;
		}catch(DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"There was an error in 'author/changeAuthorLastName' with the JPA Data Access:\n "+e.toString());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Unknown Error in 'author/changeAuthorLastName': "+e.toString());
		}	
	}
}
