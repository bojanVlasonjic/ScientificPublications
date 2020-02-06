import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { SubmitionViewDto } from 'src/app/models/submitions/submition-view-dto.model';
import { ToasterService } from 'src/app/services/toaster.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-scientific-papers-view',
  templateUrl: './scientific-papers-view.component.html',
  styleUrls: ['./scientific-papers-view.component.css']
})
export class ScientificPapersViewComponent implements OnInit {

  publishedPapers: Array<SubmitionViewDto>;

  paperToDownload: SubmitionViewDto;
  downloadPopUpDisplayed: boolean;

  constructor(
    private submitionSvc: SubmitionService,
    private toastSvc: ToasterService
    ) { 
    this.publishedPapers = [];
    this.downloadPopUpDisplayed = false;
  }

  ngOnInit() {
    this.submitionSvc.getPublishedSubmitions().subscribe(
      data => {
        this.publishedPapers = data;
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    );
  }

  viewPaper(paperId: string) {
    window.open(`${environment.baseUrl}/api/scientific-paper/view/${paperId}`, '_blank');
  }

  closePopUp(event: any) {
    this.downloadPopUpDisplayed = event;
  }

}
