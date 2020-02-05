package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.exception.ApiAuthException;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessControlService {

    private Map<SubmitionStatus, List<SubmitionStatus>> stateMachine = new HashMap<>();

    @PostConstruct
    public void init() {
        stateMachine.put(SubmitionStatus.NEW, Arrays.asList(SubmitionStatus.REJECTED, SubmitionStatus.IN_REVIEW_PROCESS, SubmitionStatus.CANCELED));
        stateMachine.put(SubmitionStatus.CANCELED, Arrays.asList());
        stateMachine.put(SubmitionStatus.REJECTED, Arrays.asList());
        stateMachine.put(SubmitionStatus.IN_REVIEW_PROCESS, Arrays.asList(SubmitionStatus.REVIEWED));
        stateMachine.put(SubmitionStatus.REVIEWED, Arrays.asList(SubmitionStatus.PUBLISHED, SubmitionStatus.REVISIONS_REQUESTED));
        stateMachine.put(SubmitionStatus.REVISIONS_REQUESTED, Arrays.asList(SubmitionStatus.REVISED, SubmitionStatus.CANCELED));
        stateMachine.put(SubmitionStatus.REVISED, Arrays.asList(SubmitionStatus.IN_REVIEW_PROCESS, SubmitionStatus.PUBLISHED, SubmitionStatus.REJECTED));
        stateMachine.put(SubmitionStatus.PUBLISHED, Arrays.asList());
    }

    public void checkIfTransitionIsPossible(SubmitionStatus currentState, SubmitionStatus newState) {
        if (!stateMachine.get(currentState).contains(newState)) {
            throw new ApiBadRequestException("This operation is not possible in current state of submition.");
        }
    }

    public void checkIfUserOwnsSubmition(Author user, Submition submition) {
        if (!(submition.getAuthor().getId() == user.getId())) {
            throw new ApiAuthException("You are unauthorized to access this submition.");
        }
    }

    public void checkIfRequestReviewIsPossible(Submition submition) {
        if (submition.getStatus() != SubmitionStatus.NEW) {
            throw new ApiBadRequestException("Reviewers are already added.");
        }
    }

    public void checkIfUserCanAcceptDeclineReviewRequest(Author user, Submition submition) {
        if (!user.getRequestedSubmitions().stream().anyMatch(reqSub -> reqSub.getId() == submition.getId())) {
            throw new ApiAuthException("You are unauthorized to accept/reject review requests for this submition.");
        }
    }

    public void checkIfUserIsRequestedToReviewSubmition(Author user, Submition submition) {
        if (user.getRequestedSubmitions().stream().anyMatch(reqSub -> reqSub.getId() == submition.getId())) {
            throw new ApiAuthException("User is already requested to review this submition.");
        }
    }

    public void checkIfUserIsReviewerForSubmition(Author user, Submition submition) {
        if (!user.getReviewedSubmitions().stream().anyMatch(revSub -> revSub.getId() == submition.getId())) {
            throw new ApiAuthException("You are unauthorized to accept/reject review requests for this submition.");
        }
    }
    
    public void checkIfUserIsNotReviewerForSubmition(Author user, Submition submition) {
        if (user.getReviewedSubmitions().stream().anyMatch(revSub -> revSub.getId() == submition.getId())) {
            throw new ApiAuthException("You are unauthorized to accept/reject review requests for this submition.");
        }
    }

}
