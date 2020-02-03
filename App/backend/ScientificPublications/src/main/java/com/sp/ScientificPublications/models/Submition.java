package com.sp.ScientificPublications.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Submition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String paperId;

    @NonNull
    @Column(nullable = false)
    private String paperTitle;

    @NonNull
    @Column(nullable = false)
    private String coverLetterId;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmitionStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Author author;

    @ManyToMany(mappedBy = "submitions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Author> reviewers = new HashSet<>();

    @ManyToMany(mappedBy = "requestedSubmitions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Author> requestedReviewers = new HashSet<>();

    @OneToMany(mappedBy = "submition",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();
}
