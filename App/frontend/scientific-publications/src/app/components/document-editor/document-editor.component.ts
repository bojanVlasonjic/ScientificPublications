import { Component, OnInit, Input } from '@angular/core';
import { CoverLetterService } from 'src/app/services/cover-letter.service';
import { DocumentReviewService } from 'src/app/services/document-review.service';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { Document } from 'src/app/models/document';

@Component({
  selector: 'app-document-editor',
  templateUrl: './document-editor.component.html',
  styleUrls: ['./document-editor.component.css']
})
export class DocumentEditorComponent implements OnInit {

  @Input() type: string;

  document: Document;
  documentValid: boolean;
  errorMessage: string;

  constructor(
    private coverLetterSvc: CoverLetterService,
    private documentReviewSvc: DocumentReviewService,
    private scientificPaperSvc: ScientificPaperService
  ) { }

  ngOnInit() {
    this.document = new Document();
    this.documentValid = true;
    this.errorMessage = '';
  }

  /***** VALIDATION ******/

  validateDocument($event) {
    this.document.documentContent = $event;

    if (this.type == 'document-review') {
      this.validateDocumentReview();
    }
    else if (this.type == 'scientific-paper') {
      this.validateScientificPaper();
    }
    else if (this.type == 'cover-letter') {
      this.validateCoverLetter();
    }
  }

  validateCoverLetter() {
    this.coverLetterSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.errorMessage = '';
      },
      error => {
        this.getErrorData(error);
      }
    );
  }

  validateScientificPaper() {
    this.scientificPaperSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.errorMessage = '';
      },
      error => {
        this.getErrorData(error);
      }
    );
  }

  validateDocumentReview() {
    this.documentReviewSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.errorMessage = '';
      },
      error => {
        this.getErrorData(error);
      }
    );
  }

  getErrorData(error: any) {
    this.documentValid = false;
    this.errorMessage = `Error at line ${error.error.lineNumber}, column ${error.error.column}, ${error.error.message}`;
  }

  /***** UPLOAD ******/

  uploadDocument($event) {
    this.document.documentContent = $event;

    if (this.type == 'document-review') {
      this.uploadDocumentReview($event);
    }
    else if (this.type == 'scientific-paper') {
      this.uploadScientificPaper($event);
    }
    else if (this.type == 'cover-letter') {
      this.uploadCoverLetter($event);
    }
  }

  uploadDocumentReview($event) {
    this.documentReviewSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      }
    )
  }

  uploadScientificPaper($event) {

    this.scientificPaperSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      } 
    );
  }

  uploadCoverLetter($event) {
    this.coverLetterSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      }
    )
  }



}
