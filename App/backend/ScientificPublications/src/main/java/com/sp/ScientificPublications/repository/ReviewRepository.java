package com.sp.ScientificPublications.repository;

import com.sp.ScientificPublications.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
