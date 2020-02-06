import { Component, OnInit, OnDestroy } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ToasterService } from 'src/app/services/toaster.service';
import { ReviewersService } from 'src/app/services/reviewers.service';
import { trigger, transition, style, animate } from '@angular/animations';
import { Subscription } from 'rxjs';
import { UploadService } from 'src/app/services/upload.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-pending-reviews',
  templateUrl: './pending-reviews.component.html',
  styleUrls: ['./pending-reviews.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: '0' }),
        animate('.5s ease-out', style({ opacity: '1' })),
      ]),
    ]),
  ],
})
export class PendingReviewsComponent implements OnInit, OnDestroy {

  private submitions = [];
  private myAcceptedSubmitions = [];
  private currentlyReviewingSubmition = null;

  private documentValid = true;
  private logMessage = '';
  private template = '';

  private currentAuthor = null;

  private editorSubscription: Subscription;

  constructor(private submitionService: SubmitionService,
              private reviewersService: ReviewersService,
              private scientificPaperService: ScientificPaperService,
              private sanitizer: DomSanitizer,
              private toaster: ToasterService,
              private uploadSvc: UploadService,
              private authService: AuthenticationService) { }

  ngOnInit() {
    this.editorSubscription = this.uploadSvc.getEditorContent().subscribe(
      data => {
        this.uploadPaperReview(data);
        console.log(data);
      }
    );
    this.getPendingReviewsForCurrentReviewer();
    this.getMyAcceptedSubmitionReviews();
  }

  ngOnDestroy() {
    this.editorSubscription.unsubscribe();
  }

  uploadPaperReview(data): void{
    this.displayUploadSpinner();
    this.reviewersService.uploadPaperReview({
      submitionId: this.currentlyReviewingSubmition.submitionId,
      reviewContent: data
    }).subscribe(
      data => {
        console.log(data);
        this.currentlyReviewingSubmition = null;
        this.getMyAcceptedSubmitionReviews();
        this.hideUploadSpinner();
      },
      error => {
        console.log(error);
        this.hideUploadSpinner();
      }
    );
  }

  selectSubmitionForPaperReview(submition) {
    this.currentlyReviewingSubmition = submition;
  }

  getPendingReviewsForCurrentReviewer(): void {
    this.reviewersService.getPendingReviewsForCurrentReviewer().subscribe(
      data => {
        this.submitions = data;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  getMyAcceptedSubmitionReviews(): void {
    this.reviewersService.getMyAcceptedSubmitionReviews().subscribe(
      data => {
        
        this.myAcceptedSubmitions = data;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  acceptSubmition(submition): void {
    this.submitions.splice(this.submitions.indexOf(submition), 1);
    this.reviewersService.acceptSubmitionReviewRequest(submition.id).subscribe(
      data => {
        this.toaster.showMessage("Accepted", "Accepted review request");
        this.getMyAcceptedSubmitionReviews();
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  declineSubmition(submition): void {
    this.submitions.splice(this.submitions.indexOf(submition), 1);
    this.reviewersService.rejectSubmitionReviewRequest(submition.id).subscribe(
      data => {
        this.toaster.showMessage("Declined", "Declined review request");
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  displayUploadSpinner() {
    document.getElementById('editor-toolbar-spinner').style.visibility = 'visible';
  }

  hideUploadSpinner() {
    document.getElementById('editor-toolbar-spinner').style.visibility = 'hidden';
  }

}
