package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmail(String email);
}
