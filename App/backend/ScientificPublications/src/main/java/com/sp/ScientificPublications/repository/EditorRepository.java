package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Editor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EditorRepository extends JpaRepository<Editor, Long> {
    Optional<Editor> findByEmail(String email);
}
