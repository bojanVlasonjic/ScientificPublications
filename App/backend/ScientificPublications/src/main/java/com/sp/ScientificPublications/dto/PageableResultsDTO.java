package com.sp.ScientificPublications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageableResultsDTO<T> {
    List<T> results;
    int numberOfPages;
}
