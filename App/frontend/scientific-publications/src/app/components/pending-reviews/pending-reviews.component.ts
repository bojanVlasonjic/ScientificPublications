import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ToasterService } from 'src/app/services/toaster.service';
import { ReviewersService } from 'src/app/services/reviewers.service';

@Component({
  selector: 'app-pending-reviews',
  templateUrl: './pending-reviews.component.html',
  styleUrls: ['./pending-reviews.component.css']
})
export class PendingReviewsComponent implements OnInit {

  private submitions = [];

  constructor(private submitionService: SubmitionService,
              private reviewersService: ReviewersService,
              private scientificPaperService: ScientificPaperService,
              private sanitizer: DomSanitizer,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.reviewersService.getPendingReviewsForCurrentReviewer().subscribe(
      data => {
        this.submitions = data;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  acceptSubmition(submition): void {
    this.submitions.splice(this.submitions.indexOf(submition));
    this.reviewersService.acceptSubmitionReviewRequest(submition.id).subscribe(
      data => {
        this.toaster.showMessage("Accepted", "Accepted review request");
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  declineSubmition(submition): void {
    this.submitions.splice(this.submitions.indexOf(submition));
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



}
