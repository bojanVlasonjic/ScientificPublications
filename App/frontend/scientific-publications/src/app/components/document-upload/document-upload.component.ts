import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { DocumentReviewService } from 'src/app/services/document-review.service';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { CoverLetterService } from 'src/app/services/cover-letter.service';
import { PaperReviewService } from 'src/app/services/paper-review.service';

@Component({
  selector: 'div [app-document-upload]',
  templateUrl: './document-upload.component.html',
  styleUrls: ['./document-upload.component.css']
})
export class DocumentUploadComponent implements OnInit {

  @ViewChild('xmlFile', {static: false}) xmlFile: ElementRef;
  private fileName: string = 'No file chosen';
  private xmlFileIndicator: string = '';
  private isValid: boolean = false;
  @Input() type: string;
  @Input() cid: string;

  constructor(
    private documentReviewService: DocumentReviewService,
    private scientificPaperService: ScientificPaperService,
    private coverLetterService: CoverLetterService,
    private paperReviewService: PaperReviewService
  ) { }

  ngOnInit() {

  }

  newFileSelected(event) {
    var input = <HTMLInputElement> event.target;
    if (input.files.length == 0) return;
    var file = input.files[0];
    this.fileName = file.name;
    this.sendDocumentForValidation();
  }

  sendDocument() {
    var input = <HTMLInputElement> this.xmlFile.nativeElement;
    if (input.files.length == 0) return;
    if (!this.isValid) {
      this.xmlFileIndicator = "Cannot send invalid document";
      return;
    }
    var form = new FormData();
    form.append('file', input.files[0]);

    if (this.type == 'document-review'){
      this.documentReviewService.uploadFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is successfully uploaded";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Something went wrong";
          console.log(error);
        }
      );
    }

    else if (this.type == 'paper-review') {
      this.paperReviewService.uploadFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is successfully uploaded";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Something went wrong";
          console.log(error);
        }
      );
    }
    else if (this.type == 'scientific-paper') {
      this.scientificPaperService.uploadFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is successfully uploaded";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Something went wrong";
          console.log(error);
        }
      );
    }
    else if (this.type == 'cover-letter') {
      this.coverLetterService.uploadFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is successfully uploaded";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Something went wrong";
          console.log(error);
        }
      );
    }
  }

  sendDocumentForValidation() {
    var input = <HTMLInputElement> this.xmlFile.nativeElement;
    if (input.files.length == 0) return;
    var form = new FormData();
    form.append('file', input.files[0]);

    if (this.type == 'document-review'){
      this.documentReviewService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          this.isValid = true;
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          this.isValid = false;
          console.log(error);
        }
      );
    }
    else if (this.type == 'paper-review') {
      this.paperReviewService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          this.isValid = true;
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          this.isValid = false;
          console.log(error);
        }
      );
    }
    else if (this.type == 'scientific-paper') {
      this.scientificPaperService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          this.isValid = true;
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          this.isValid = false;
          console.log(error);
        }
      );
    }
    else if (this.type == 'cover-letter') {
      this.coverLetterService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          this.isValid = true;
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          this.isValid = false;
          console.log(error);
        }
      );
    }
  }

}
