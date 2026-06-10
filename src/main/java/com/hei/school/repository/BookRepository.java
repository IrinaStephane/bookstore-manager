package com.hei.school.repository;

import com.hei.school.entity.Book;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, UUID> {

  @Query(
      value = "SELECT COUNT(*) > 0 FROM book WHERE LOWER(title) = LOWER(:title)",
      nativeQuery = true)
  boolean existsByTitleIgnoreCase(@Param("title") String title);

  @Query(
      value = "SELECT * FROM book WHERE language = :language",
      countQuery = "SELECT COUNT(*) FROM book WHERE language = :language",
      nativeQuery = true)
  Page<Book> findByLanguage(@Param("language") String language, Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT b.* FROM book b"
              + " JOIN book_genre bg ON b.id = bg.book_id"
              + " WHERE bg.genre_id = :genreId",
      countQuery =
          "SELECT COUNT(DISTINCT b.id) FROM book b"
              + " JOIN book_genre bg ON b.id = bg.book_id"
              + " WHERE bg.genre_id = :genreId",
      nativeQuery = true)
  Page<Book> findByGenreId(@Param("genreId") UUID genreId, Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT b.* FROM book b"
              + " JOIN book_author ba ON b.id = ba.book_id"
              + " WHERE ba.author_id = :authorId",
      countQuery =
          "SELECT COUNT(DISTINCT b.id) FROM book b"
              + " JOIN book_author ba ON b.id = ba.book_id"
              + " WHERE ba.author_id = :authorId",
      nativeQuery = true)
  Page<Book> findByAuthorId(@Param("authorId") UUID authorId, Pageable pageable);

  @Query(
      value =
          "SELECT * FROM book"
              + " WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))",
      countQuery =
          "SELECT COUNT(*) FROM book"
              + " WHERE LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%'))",
      nativeQuery = true)
  Page<Book> searchByTitleOrDescription(@Param("search") String search, Pageable pageable);

  @Query(
      value =
          "SELECT * FROM book"
              + " WHERE language = :language"
              + " AND (LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%')))",
      countQuery =
          "SELECT COUNT(*) FROM book"
              + " WHERE language = :language"
              + " AND (LOWER(title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(description) LIKE LOWER(CONCAT('%', :search, '%')))",
      nativeQuery = true)
  Page<Book> findByLanguageAndSearch(
      @Param("language") String language, @Param("search") String search, Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT b.* FROM book b"
              + " JOIN book_genre bg ON b.id = bg.book_id"
              + " WHERE bg.genre_id = :genreId"
              + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))",
      countQuery =
          "SELECT COUNT(DISTINCT b.id) FROM book b"
              + " JOIN book_genre bg ON b.id = bg.book_id"
              + " WHERE bg.genre_id = :genreId"
              + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))",
      nativeQuery = true)
  Page<Book> findByGenreIdAndSearch(
      @Param("genreId") UUID genreId, @Param("search") String search, Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT b.* FROM book b"
              + " JOIN book_author ba ON b.id = ba.book_id"
              + " WHERE ba.author_id = :authorId"
              + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))",
      countQuery =
          "SELECT COUNT(DISTINCT b.id) FROM book b"
              + " JOIN book_author ba ON b.id = ba.book_id"
              + " WHERE ba.author_id = :authorId"
              + " AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))"
              + " OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%')))",
      nativeQuery = true)
  Page<Book> findByAuthorIdAndSearch(
      @Param("authorId") UUID authorId, @Param("search") String search, Pageable pageable);
}
