import { Component, OnInit } from '@angular/core';
import { DocumentReviewService } from 'src/app/services/document-review.service';
import { DocumentDTO } from 'src/app/models/documentDTO';

@Component({
  selector: 'app-document-review-editor',
  templateUrl: './document-review-editor.component.html',
  styleUrls: ['./document-review-editor.component.css']
})
export class DocumentReviewEditorComponent implements OnInit {

  document: DocumentDTO;
  template: string;
  
  documentValid: boolean;
  errorMessage: string;

  constructor(private docReviewSvc: DocumentReviewService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.errorMessage = '';

    this.getTemplate();
  }

  getTemplate() {
    this.docReviewSvc.generateTemplate().subscribe(
      data => {
        this.template = data.documentContent;
      },
      error => {
        console.log(error.error);
      }
    );
  }


  validateDocument($event) {
    this.document.documentContent = $event;

    this.docReviewSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.errorMessage = '';
      },
      error => {
        this.documentValid = false;
        this.errorMessage = `Error at line ${error.error.lineNumber}, column ${error.error.column}, ${error.error.message}`;
      }
    );
  }


  uploadDocument($event) {
    this.document.documentContent = $event;

    this.docReviewSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      },
      err => {
        console.log(err.error);
      }
    );
    
  }

}
