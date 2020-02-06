package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmitionRepository extends JpaRepository<Submition, Long> {
	Submition findByPaperIdAndStatus(String paperId, SubmitionStatus status);
	Submition findByPaperId(String paperId);
	List<Submition> findAllByStatus(SubmitionStatus status);
	List<Submition> findAllByRequestedReviewersContaining(Author reviewer);
}
