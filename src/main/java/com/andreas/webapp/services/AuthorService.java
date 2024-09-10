package com.andreas.webapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.andreas.webapp.dao.AuthorRepo;
import com.andreas.webapp.model.Author;

@Service
@Component
public class AuthorService {

	@Autowired
	AuthorRepo authRepo;
	
	
	/**
	 * Function to save a new author in the database
	 * beforehand, it is checked that the first and last name provided are valid, e.g. not null
	 * @param athr - an Object of type "Author"
	 * @return - In case of successful creation, the new author object is returned in the responseEntity
	 * 		- if first or last name are not valid, the ResponseEntity contains a message informing the user of this
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> createAuthorService(Author athr) throws Exception{
		
		if(athr.getFirstName() == null||athr.getLastName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"First Name AND Last Name must be provided to create a new author!");
		}
		athr = authRepo.save(athr);
		
		return new ResponseEntity<>(athr,HttpStatus.OK);
	}
	
	
	/**
	 * A function used to return an Author corresponding to a particular ID
	 * the functions checks whether the function ID is legit
	 * it also checks whether an Author corresponding to the ID was found or not 
	 * @param authorID - ID of the author we are searching for
	 * @return - If an author matching the ID is found, he is returned as an object in the responseEntity
	 * 		- if author-ID not valid, or no Author with the ID is found, the ResponseEntity 
	 * 		 contains a message informing the user of this
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> getAuthorByIdService(int authorID) throws Exception {
		
		if(authorID <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"Invalid Author-ID provided!");
		}
		
		Optional<Author> optionalAuth = authRepo.findById(authorID);
		
		if(optionalAuth.isPresent()) {
			return new ResponseEntity<>(optionalAuth.get(),HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No Author found for the provided Author-ID!");
		}
	}

	
	/**
	 * Returns ALL authors present in the database
	 * @return the ResponseEntity contains a LIST with all present Authors
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> getAllAuthorsService() throws Exception {
		List<Author> authorList = authRepo.findAll();
		return new ResponseEntity<>(authorList,HttpStatus.OK);
	}

	
	/**
	 * returns a list of all authors whose first or last names match the input
	 * the input must not be complete, it's sufficient if the input is contained
	 * 
	 * first, it is checked that the provided names are not empty
	 * @param firstName - first name of the author we are searching for
	 * @param lastName - last name of the author we are searching for
	 * @return - the ResponseEntity contains a list of all authors matching the input
	 * 			- if the provided first or last name were not valid, a ResponseEntity is returned
	 * 			  informing the caller of this
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> getByNamesService(String firstName,String lastName) throws Exception {
		
		if(firstName==null || firstName.equals("") || lastName==null || lastName.equals("")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"A value fpr First Name AND Last Name must be provided for the author search!");
		}
		
		List<Author> resultSet = authRepo.findByNames(firstName,lastName);
	
		return new ResponseEntity<>(resultSet,HttpStatus.OK);
	}

	
	/**
	 * A function that can delete an author with the corresponding ID from the database
	 * checks first if an author with that ID even exists
	 * @param authorID - ID of the author we want to delete
	 * @return - in case of successful deletion, a ResponseEntity is returned with a message 
	 * 			confirming the deletion
	 * 		   - in case an author with said ID didn't exist, the ResponseEntity insteead informs
	 * 		     the caller of this
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> deleteAuthorByIdService(int authorID) throws Exception {
		
		if(authRepo.existsById(authorID)) {
			authRepo.deleteById(authorID);
			return ResponseEntity.status(HttpStatus.OK).body(
					"Author successfully deleted!");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No Author found for the provided Author-ID!");
		}
		
	}
	
	
	/**
	 * A function that can delete ALL authors in the database
	 * @return - A ResponseEntity with a message confirming the deletion of all authors is returned
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> deleteAllService() throws Exception {
		
		authRepo.deleteAll();
		return ResponseEntity.status(HttpStatus.OK).body(
				"All Authors successfully deleted!");
	}

	
	/**
	 * A function used to update the first name of an existing author (has an existing author ID)
	 * it is also checked whether the author to-be-updated even exists
	 * @param authorID - ID of the author whose first name we want to update
	 * @param firstName - first name we want to update to
	 * @return - If successful, a ResponseEntity with the updated author is returned
	 * 		   - if the author with the ID doesn't exist, the ResponseEntity informs the caller of this with a message
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> updateFirstNameService(int authorID, String firstName) throws Exception {
		Optional<Author> newAuth = authRepo.findById(authorID);
		
		if(newAuth.isPresent()) {
			Author result = newAuth.get();
			result.setFirstName(firstName);
			result = authRepo.save(result);
		
			return new ResponseEntity<>(result,HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No Author found for the provided Author-ID!");
		}
	}

	
	/**
	 * A function used to update the last name of an existing author (has an existing author ID)
	 * it is also checked whether the author to-be-updated even exists
	 * @param authorID - ID of the author whose last name we want to update
	 * @param firstName - last name we want to update to
	 * @return - If successful, a ResponseEntity with the updated author is returned
	 * 		   - if the author with the ID doesn't exist, the ResponseEntity informs the caller of this with a message
	 * @throws Exception - passes any exception on to the API-class that called it
	 */
	public ResponseEntity<Object> updateLastNameService(int authorID, String lastName) throws Exception {
		Optional<Author> newAuth = authRepo.findById(authorID);
		
		if(newAuth.isPresent()) {
			Author result = newAuth.get();
			result.setLastName(lastName);
			result = authRepo.save(result);
		
			return new ResponseEntity<>(result,HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No Author found for the provided Author-ID!");
		}
	}
}
