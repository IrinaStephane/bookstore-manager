package com.hei.school.repository;

import com.hei.school.entity.Book;
import com.hei.school.entity.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

  boolean existsByTitleIgnoreCase(String title);

  Page<Book> findByLanguage(Language language, Pageable pageable);

  @Query("SELECT DISTINCT b FROM Book b JOIN b.genres g WHERE g.id = :genreId")
  Page<Book> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);

  @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
  Page<Book> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

  @Query(
      "SELECT b FROM Book b"
          + " WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
          + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%'))")
  Page<Book> searchByTitleOrDescription(@Param("search") String search, Pageable pageable);

  @Query(
      "SELECT b FROM Book b"
          + " WHERE b.language = :language"
          + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
          + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<Book> findByLanguageAndSearch(
      @Param("language") Language language, @Param("search") String search, Pageable pageable);

  @Query(
      "SELECT DISTINCT b FROM Book b JOIN b.genres g"
          + " WHERE g.id = :genreId"
          + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
          + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<Book> findByGenreIdAndSearch(
      @Param("genreId") Long genreId, @Param("search") String search, Pageable pageable);

  @Query(
      "SELECT DISTINCT b FROM Book b JOIN b.authors a"
          + " WHERE a.id = :authorId"
          + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
          + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<Book> findByAuthorIdAndSearch(
      @Param("authorId") Long authorId, @Param("search") String search, Pageable pageable);
}
