package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Review;
import com.sp.ScientificPublications.models.Submition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllBySubmition(Submition submition);
}
