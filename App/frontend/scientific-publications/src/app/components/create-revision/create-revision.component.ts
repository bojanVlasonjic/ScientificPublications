import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ReviewersService } from 'src/app/services/reviewers.service';
import { ReviewDto } from 'src/app/models/review/review-dto.model';
import { UploadService } from 'src/app/services/upload.service';
import { environment } from 'src/environments/environment';
import { CreateSubmitionDTO } from 'src/app/models/submitions/create-submition-dto.model';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { SubmitionService } from 'src/app/services/submition.service';

@Component({
  selector: 'app-create-revision',
  templateUrl: './create-revision.component.html',
  styleUrls: ['./create-revision.component.css']
})
export class CreateRevisionComponent implements OnInit {

  uploadType: string // file or editor
  reviewsForPaper: Array<ReviewDto>;

  paperId: string;
  paperContent: string;
  submitionId: number;

  @Output() submitionUpdatedEvent: EventEmitter<any>;

  constructor(
    private reviewSvc: ReviewersService,
    private scientificPaperSvc: ScientificPaperService,
    private submitionSvc: SubmitionService,
    private uploadSvc: UploadService,
    private toastSvc: ToasterService
    ) {
      this.submitionUpdatedEvent = new EventEmitter();
    }

  @Input()
  set reviewedPaperId(id: string) {
    this.paperId = id;

    if(id != null) {
      this.getPaperReviews();
      this.getPaperContent();
      this.uploadType = '';
    }
  }

  @Input()
  set reviewedSubmitionId(id: number) {
    this.submitionId = id;
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

  getPaperContent() {

    this.scientificPaperSvc.getPaperById(this.paperId).subscribe(
      data => {
        this.paperContent = data.documentContent;
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    );

  }

  waitForEditorContent() {

    this.uploadSvc.getEditorContent().subscribe(
      data => {
        this.uploadUpdatedPaper(data);
      },
      err => {
        console.log(err.error);
      }
    );
  }

  uploadUpdatedPaper(paperContent: string) {

    let submitionDTO = new CreateSubmitionDTO();
    submitionDTO.paperContent = paperContent;

    this.displayUploadSpinner();

    this.submitionSvc.reviseSubmition(this.submitionId, submitionDTO).subscribe(
      data => {
        this.toastSvc.showMessage('Success', 'Your revision was successfully sent');
        this.uploadType = '';
        this.submitionUpdatedEvent.emit(data);
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    ).add(
      () => {
        this.hideUploadSpinner(); 
      });

  }

  viewReview(reviewId: string) {
    window.open(`${environment.baseUrl}/api/paper-review/view/${reviewId}`, '_blank');
  }

  uploadPaperRevision(paperContent: string) {
    let dto = new CreateSubmitionDTO
  }

  displayUploadSpinner() {
    document.getElementById('editor-toolbar-spinner').style.visibility = 'visible';
  }

  hideUploadSpinner() {
    document.getElementById('editor-toolbar-spinner').style.visibility = 'hidden';
  }


}
