package com.andreas.webapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andreas.webapp.model.Author;

public interface AuthorRepo extends JpaRepository<Author,Integer>{

	
	/**
	 * returns a list of all authors whose first or last names match the input
	 * the input must not be complete, it's sufficient if the input is contained
	 * 
	 * first, it is checked that the provided names are not empty
	 * @param firstName - first name of the author we are searching for
	 * @param lastName - last name of the author we are searching for
	 * @return - List of authors whose names matched the input
	 */
	@Query(value = "SELECT * FROM AUTHOR WHERE "
			+ "FIRST_NAME LIKE %:firstName% OR LAST_NAME LIKE %:lastName%", 
			nativeQuery = true)
	List<Author> findByNames(@Param("firstName") String firstName,@Param("lastName")String lastName);
}
