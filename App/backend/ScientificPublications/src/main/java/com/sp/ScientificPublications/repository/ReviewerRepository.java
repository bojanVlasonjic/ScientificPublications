package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {
}
