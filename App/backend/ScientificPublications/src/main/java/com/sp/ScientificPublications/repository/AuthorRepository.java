package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
