import { Component, OnInit } from '@angular/core';
import { AuthorSubmitionDTO } from 'src/app/models/submitions/author-submition-dto.model';
import { SubmitionService } from 'src/app/services/submition.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-my-scientific-papers',
  templateUrl: './my-scientific-papers.component.html',
  styleUrls: ['./my-scientific-papers.component.css']
})
export class MyScientificPapersComponent implements OnInit {

  submitions: Array<AuthorSubmitionDTO>;

  displayPopUp: boolean;
  submitionToDownload: AuthorSubmitionDTO;

  constructor(private submitionSvc: SubmitionService , private toastSvc: ToasterService) {
    this.submitions = [];
    this.displayPopUp = false;

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
    this.displayPopUp = true;
    this.submitionToDownload = submition;
  }

  closePopUp(event: any) {
    this.displayPopUp = event;
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

}
