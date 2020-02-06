import { Component, OnInit } from '@angular/core';
import { DocumentDTO } from 'src/app/models/document-dto';
import { PaperReviewService } from 'src/app/services/paper-review.service';

@Component({
  selector: 'app-paper-review-editor',
  templateUrl: './paper-review-editor.component.html',
  styleUrls: ['./paper-review-editor.component.css']
})
export class PaperReviewEditorComponent implements OnInit {

  template: string;
  document: DocumentDTO;

  documentValid: boolean;
  logMessage: string;

  constructor(private paperReviewService: PaperReviewService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.logMessage = '';

    this.getTemplate();
  }

  getTemplate() {
    this.paperReviewService.generateTemplate().subscribe(
      data => {
        this.template = data.documentContent;
      },
      error => {
        console.log(error.error);
      }
    );
  }

  validateDocument(event: any) {
    this.document.documentContent = event;

    this.paperReviewService.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.logMessage = '';
      },
      error => {
        this.documentValid = false;
        this.logMessage = `Error at line ${error.error.lineNumber}, column ${error.error.column}, ${error.error.message}`;
      }
    );
  }

}
