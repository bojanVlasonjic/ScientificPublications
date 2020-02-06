import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-document-download',
  templateUrl: './document-download.component.html',
  styleUrls: ['./document-download.component.css']
})
export class DocumentDownloadComponent implements OnInit {

  popUpDisplayed: boolean; // input
  submitionInPopUp: any; // input

  documentType: string; // scientific-paper or cover-letter
  downloadType: string; // html or pdf

  @Output() closePopUpEvent: EventEmitter<boolean>;

  constructor() { 
    this.downloadType = 'pdf';
    this.documentType = '';
    this.closePopUpEvent = new EventEmitter();
  }

  ngOnInit() {
  }

  @Input()
  set submition(submition: any) {
    this.submitionInPopUp = submition;
  }

  @Input()
  set displayPopUp(display: boolean) {
    this.popUpDisplayed = display;
  }

  closePopUp() {
    this.documentType = '';
    this.closePopUpEvent.emit(false);
  }

  downloadTypeChanged(typeSelected: string) {
    this.downloadType = typeSelected;
  }

  downloadDocument(documentId: string) {
    window.open(
      `${environment.baseUrl}/api/${this.documentType}/download/${this.downloadType}/${documentId}`,
      '_blank');
  }

}
