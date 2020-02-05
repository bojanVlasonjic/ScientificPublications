import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {

  private selectedPaper = null;
  private submitions = [];
  private reviewers = [];
  private requestedReviewers = [];

  constructor(private submitionService: SubmitionService,
              private scientificPaperService: ScientificPaperService,
              private sanitizer: DomSanitizer,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.submitionService.getAllSubmitions().subscribe(
      data => {
        this.submitions = data.results;
        console.log(this.submitions);
      },
      error => {
        console.log(error);
      }
    )
  }

  selectPaper(paper) {
    this.selectedPaper = paper;
    if (paper != null) {
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
    else {
      this.requestedReviewers = [];
      this.reviewers = [];
    }
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  goToLink(url: string){
    window.open(url, "_blank");
  }

  requestReview(reviewer) {
    this.reviewers.splice(this.reviewers.indexOf(reviewer));
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
    this.submitionService.getRequestedReviewers(this.selectedPaper.paperId).subscribe(
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
