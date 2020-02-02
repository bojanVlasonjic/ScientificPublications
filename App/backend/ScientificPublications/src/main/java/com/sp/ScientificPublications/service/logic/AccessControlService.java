package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    public boolean userOwnSubmition(Author user, Submition submition) {
        return submition.getAuthor().getId() != user.getId();
    }

    public boolean userIsRequestedToReviewSubmition(Author user, Submition submition) {
        return user.getRequestedSubmitions().stream().anyMatch(reqSub -> reqSub.getId() == submition.getId());
    }

    public boolean userIsReviewerForSubmition(Author user, Submition submition) {
        return user.getReviewedSubmitions().stream().anyMatch(revSub -> revSub.getId() == submition.getId());
    }

}
