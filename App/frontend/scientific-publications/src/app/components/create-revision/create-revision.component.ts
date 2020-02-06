import { Component, OnInit, Input } from '@angular/core';
import { ReviewersService } from 'src/app/services/reviewers.service';
import { ReviewDto } from 'src/app/models/review/review-dto.model';
import { UploadService } from 'src/app/services/upload.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-create-revision',
  templateUrl: './create-revision.component.html',
  styleUrls: ['./create-revision.component.css']
})
export class CreateRevisionComponent implements OnInit {

  uploadType: string // file or editor
  reviewsForPaper: Array<ReviewDto>;

  paperId: string;

  constructor(
    private reviewSvc: ReviewersService,
    private uploadSvc: UploadService
    ) { }

  @Input()
  set reviewedPaper(id: string) {
    this.paperId = id;

    if(id != null) {
      this.getPaperReviews();
    }
  }

  ngOnInit() {
    this.waitForEditorContent();
  }

  getPaperReviews() {
    this.reviewSvc.getReviewsForPaper(this.paperId).subscribe(
      data => {
        this.reviewsForPaper = data;
      },
      err => {
        console.log(err.error);
      }
    );
  }

  waitForEditorContent() {

    this.uploadSvc.getEditorContent().subscribe(
      data => {

      },
      err => {

      }
    );
  }

  viewReview(reviewId: string) {
    window.open(`${environment.baseUrl}/api/document-review/view/${reviewId}`, '_blank');
  }

}
