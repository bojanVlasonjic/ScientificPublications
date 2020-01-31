import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { DocumentReviewService } from 'src/app/services/document-review.service';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';
import { CoverLetterService } from 'src/app/services/cover-letter.service';

@Component({
  selector: 'div [app-document-upload]',
  templateUrl: './document-upload.component.html',
  styleUrls: ['./document-upload.component.css']
})
export class DocumentUploadComponent implements OnInit {

  @ViewChild('xmlFile', {static: false}) xmlFile: ElementRef;
  private fileName: string = 'No file chosen';
  private xmlFileIndicator: string = '';
  @Input() type: string;
  @Input() cid: string;

  constructor(
    private documentReviewService: DocumentReviewService,
    private scientificPaperService: ScientificPaperService,
    private coverLetterService: CoverLetterService
  ) { }

  ngOnInit() {

  }

  newFileSelected(event) {
    var input = <HTMLInputElement> event.target;
    if (input.files.length == 0) return;
    var file = input.files[0];
    this.fileName = file.name;
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
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          console.log(error);
        }
      );
    }
    else if (this.type == 'scientific-paper') {
      this.scientificPaperService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          console.log(error);
        }
      );
    }
    else if (this.type == 'cover-letter') {
      this.coverLetterService.validateFile(form).subscribe(
        data => {
          this.xmlFileIndicator = "Document is valid";
          console.log(data);
        },
        error => {
          this.xmlFileIndicator = "Document is not valid";
          console.log(error);
        }
      );
    }
  }

}
