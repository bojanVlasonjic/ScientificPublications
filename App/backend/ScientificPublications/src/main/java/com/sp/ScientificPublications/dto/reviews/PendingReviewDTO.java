package com.sp.ScientificPublications.dto.reviews;

import java.util.List;

import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.models.Submition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PendingReviewDTO {
	private Long id;
    private String paperId;
    private String paperTitle;
    private String coverLetterId;
    private String status;
    private UserDTO author;
    
    public PendingReviewDTO(Submition submition) {
    	id = submition.getId();
    	paperId = submition.getPaperId();
    	paperTitle = submition.getPaperTitle();
    	coverLetterId = submition.getCoverLetterId();
    	status = submition.getStatus().toString();
    	author = new UserDTO(submition.getAuthor());
    }
}
