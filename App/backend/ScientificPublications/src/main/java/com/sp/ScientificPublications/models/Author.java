package com.sp.ScientificPublications.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public class Author extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "author_submitions",
        joinColumns = @JoinColumn(name = "author_id"),
        inverseJoinColumns = @JoinColumn(name = "submition_id"))
    private Set<Submition> submitions = new HashSet<>();
}
