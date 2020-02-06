import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { trigger, transition, style, animate } from '@angular/animations';
import { EditorService } from 'src/app/services/editor.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: '0' }),
        animate('.5s ease-out', style({ opacity: '1' })),
      ]),
    ]),
  ],
})
export class EditorComponent implements OnInit {

  private selectedPaper = null;
  private submitions = [];
  private reviewers = [];
  private requestedReviewers = [];
  private reviewsForSelectedSubmition = [];

  constructor(private submitionService: SubmitionService,
              private scientificPaperService: ScientificPaperService,
              private sanitizer: DomSanitizer,
              private editorService: EditorService,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.getAllSubmitions();
  }

  getAllSubmitions() {
    this.submitionService.getAllSubmitions().subscribe(
      data => {
        this.submitions = data.results;
        console.log(this.submitions);
      },
      error => {
        console.log(error);
      }
    );
  }

  startReviewForSubmition(): void {
    this.editorService.startReviewForSubmition(this.selectedPaper.id).subscribe(
      data => {
        this.getAllSubmitions();
        this.removeSelections();
        console.log(data);
      },
      error => {
        this.toaster.showMessage("Review", "Can not start review without reviewers");
        console.log(error);
      }
    )
  }

  acceptSubmition(): void {
    console.log(this.selectedPaper.id);
    this.editorService.acceptSubmition(this.selectedPaper.id).subscribe(
      data => {
        this.removeSelections();
        this.getAllSubmitions();
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

  requestRevisionsSubmition(): void {
    this.editorService.requestRevisionsSubmition(this.selectedPaper.id).subscribe(
      data => {
        this.getAllSubmitions();
        this.removeSelections();
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  rejectSubmition(): void {
    this.editorService.rejectSubmition(this.selectedPaper.id).subscribe(
      data => {
        this.removeSelections();
        this.getAllSubmitions();
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

  selectPaper(paper) {
    this.selectedPaper = paper;
    if (paper != null && paper.status == "NEW") {
      this.getRequestReviewers();

      this.scientificPaperService.getRecommenderReviewers(this.selectedPaper.paperId).subscribe(
        data => {
          this.reviewers = data;
          console.log(data);
        },
        error => {
          console.log(error);
        }
      )
    }
    else if (paper != null && paper.status == "REVIEWED") {
      this.editorService.getReviewsForSubmition(this.selectedPaper.id).subscribe(
        data => {
          this.reviewsForSelectedSubmition = data;
          console.log(data);
        },
        error => {
          console.log(error);
        }
      )
    }

    else if (paper != null && paper.status == "IN_REVIEW_PROCESS") {
      this.getRequestReviewers();
    }

    else if (paper != null && paper.status == "REVISED") {
      this.getRequestReviewers();
    }

    else {
      this.removeSelections();
    }
  }

  removeSelections(): void {
    this.selectedPaper = null;
    this.requestedReviewers = [];
    this.reviewers = [];
    this.reviewsForSelectedSubmition = [];
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  goToLink(url: string){
    window.open(url, "_blank");
  }

  requestReview(reviewer) {
    const index = this.reviewers.indexOf(reviewer);
    this.reviewers.splice(index, 1);
    this.submitionService.requestReview(this.selectedPaper.id, reviewer.id).subscribe(
      data => {
        this.toaster.showMessage("Success", "Review requested");
        this.getRequestReviewers();
      },
      error => {
        console.log(error);
      }
    )
  }

  getRequestReviewers(): void {
    this.submitionService.getAllReviewersForSubmition(this.selectedPaper.id).subscribe(
      data => {
        this.requestedReviewers = data;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

}
