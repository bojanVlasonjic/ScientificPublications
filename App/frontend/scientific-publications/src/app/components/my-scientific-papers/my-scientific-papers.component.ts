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

  displayDownloadPopUp: boolean;
  submitionToDownload: AuthorSubmitionDTO;

  reviewedPaperId: string;
  reviewedSubmitionId: number;
  createRevisionDisplayed: boolean; 

  constructor(private submitionSvc: SubmitionService , private toastSvc: ToasterService) {
    this.submitions = [];
    this.displayDownloadPopUp = false;
    this.createRevisionDisplayed = false;

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

  showPopUp(submition: any): void {
    this.displayDownloadPopUp = true;
    this.submitionToDownload = submition;
  }

  closePopUp(event: any) {
    this.displayDownloadPopUp = event;
  }

  viewDocument(documentType: string, documentId: string) {
    window.open(`${environment.baseUrl}/api/${documentType}/view/${documentId}`, '_blank');
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

  displayCreateRevision(submition: AuthorSubmitionDTO) {
    this.createRevisionDisplayed = true;
    this.reviewedPaperId = submition.paperId;
    this.reviewedSubmitionId = submition.submitionId;
  }

  updateSubmition(submition: any) {

    if(submition != null) {
      for(let i = 0; i < this.submitions.length; i++) {
        if(this.submitions[i].submitionId == submition.submitionId) {
          this.submitions[i] = submition;
        }
      }
    }

    this.createRevisionDisplayed = false;
    this.reviewedPaperId = null;
    this.reviewedSubmitionId = null;

  }

}
