package com.sp.ScientificPublications.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reviewer extends Author {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "reviewer_submitions",
        joinColumns = @JoinColumn(name = "reviewer_id"),
        inverseJoinColumns = @JoinColumn(name = "submition_id"))
    private Set<Submition> submitions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();
}
