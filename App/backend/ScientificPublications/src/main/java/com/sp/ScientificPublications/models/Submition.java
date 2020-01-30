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
    private String coverLetterId;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmitionStatus status;

    @ManyToMany(mappedBy = "submitions", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(mappedBy = "submitions", fetch = FetchType.LAZY)
    private Set<Reviewer> reviewers = new HashSet<>();

    @OneToMany(mappedBy = "submition",fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();
}
