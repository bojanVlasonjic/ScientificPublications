import { Component, OnInit } from '@angular/core';
import { AuthorSubmitionDTO } from 'src/app/models/submitions/author-submition-dto.model';
import { SubmitionService } from 'src/app/services/submition.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { environment } from 'src/environments/environment';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-my-scientific-papers',
  templateUrl: './my-scientific-papers.component.html',
  styleUrls: ['./my-scientific-papers.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: '0' }),
        animate('.5s ease-out', style({ opacity: '1' })),
      ]),
    ]),
  ],
})
export class MyScientificPapersComponent implements OnInit {

  submitions: Array<AuthorSubmitionDTO>;

  displayPopUp: boolean;
  submitionInPopUp: AuthorSubmitionDTO;
  documentType: string;
  downloadType: string; // html or pdf

  constructor(private submitionSvc: SubmitionService , private toastSvc: ToasterService) {
    this.submitions = [];
    this.displayPopUp = false;
    this.downloadType = 'pdf';
    this.documentType = '';
  }

  ngOnInit() {
    this.submitionSvc.getSubmitionsForAuthor().subscribe(
      data => {
        this.submitions = data;
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    );
  }

  showPopUp(submition: AuthorSubmitionDTO): void {
    this.displayPopUp = true;
    this.submitionInPopUp = submition;
  }

  closePopUp() {
    this.displayPopUp = false;
    this.documentType = '';
  }

  downloadTypeChanged(typeSelected: string) {
    this.downloadType = typeSelected;
  }

  viewDocument(documentType: string, documentId: string) {
    window.open(`${environment.baseUrl}/api/${documentType}/view/${documentId}`, '_blank');
  }

  downloadDocument(documentId: string) {
    window.open(
      `${environment.baseUrl}/api/${this.documentType}/download/${this.downloadType}/${documentId}`,
      '_blank');
  }

  cancelSubmition(id: string, index: number) {

    if(!window.confirm('Are you sure you want to cancel this submition?')) {
      return;
    }

    this.submitionSvc.cancelSubmition(id).subscribe(
      data => {
        this.submitions[index] = data;
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    );

  }

}
