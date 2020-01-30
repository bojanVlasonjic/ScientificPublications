package com.sp.ScientificPublications.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Review extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String reviewId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Reviewer reviewer;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Submition submition;

}
