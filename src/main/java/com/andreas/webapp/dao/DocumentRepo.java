package com.andreas.webapp.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andreas.webapp.model.Document;

import jakarta.transaction.Transactional;

public interface DocumentRepo extends JpaRepository<Document,Integer>{

	@Modifying
    @Query(value = "INSERT INTO AUTHORED_BY(AUTHORID,DOCUMENTID) "
    		+ "VALUES (:newAuthor,:newDocument)", nativeQuery = true)
    @Transactional
    void insertIntoAuthored(@Param("newAuthor") int insertAuth, 
    		@Param("newDocument") int insertDoc) throws Exception;
	
	@Modifying
    @Query(value = "INSERT INTO REFERENCED_BY(DOCUMENT_REFERENCED,REFERENCING_DOCUMENT) "
    		+ "VALUES (:referenced,:referencing)", nativeQuery = true)
    @Transactional
    void insertIntoReferenced(@Param("referencing") int referencing,@Param("referenced") int referenced) throws Exception;
	

	@Query(value="SELECT AUTHORID FROM AUTHORED_BY "
			+ "WHERE DOCUMENTID = ?1", nativeQuery = true)
	Set<Integer> findAuthorsOfDocument(int documentId);
	
	@Query(value="SELECT DOCUMENT_REFERENCED "
			+ "FROM REFERENCED_BY WHERE REFERENCING_DOCUMENT = ?1", nativeQuery = true)
	Set<Integer> findReferencesOfDocument(int documentId);
	
	@Modifying
	@Query(value="DELETE FROM AUTHORED_BY a WHERE a.DOCUMENTID = :documentId AND a.AUTHORID = :authorId", nativeQuery = true)
	@Transactional
	void deleteAuthoredEntries(@Param("documentId") int documentId, @Param("authorId") int authorId) throws Exception;
	
	@Modifying
	@Query(value="DELETE FROM REFERENCED_BY WHERE REFERENCING_DOCUMENT= :referencing AND DOCUMENT_REFERENCED= :referenced", nativeQuery = true)
	@Transactional
	void deleteReferencesEntries(@Param("referencing") int referencing, @Param("referenced") int referenced) throws Exception;
}
