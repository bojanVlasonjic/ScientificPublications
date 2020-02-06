package com.sp.ScientificPublications.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Submition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paperId;

    @Column(nullable = false)
    private String paperTitle;

    @Column(unique = true)
    private String coverLetterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmitionStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Author author;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreated = new Date();

    @Column(nullable =  true)
    @Temporal(TemporalType.DATE)
    private Date dateRevised;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date datePublished;

    @ManyToMany(mappedBy = "reviewedSubmitions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Author> reviewers = new HashSet<>();

    @ManyToMany(mappedBy = "requestedSubmitions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Author> requestedReviewers = new HashSet<>();

    @OneToMany(mappedBy = "submition",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    public Submition(String paperId, String title, String coverLetterId, SubmitionStatus status) {
        this.paperId = paperId;
        this.paperTitle = title;
        this.coverLetterId = coverLetterId;
        this.status = status;
    }
}
