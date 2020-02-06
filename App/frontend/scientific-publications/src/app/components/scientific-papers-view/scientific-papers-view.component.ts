import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { SubmitionViewDto } from 'src/app/models/submitions/submition-view-dto.model';
import { ToasterService } from 'src/app/services/toaster.service';
import { environment } from 'src/environments/environment';
import { SearchParams } from 'src/app/models/search-params.model';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';

@Component({
  selector: 'app-scientific-papers-view',
  templateUrl: './scientific-papers-view.component.html',
  styleUrls: ['./scientific-papers-view.component.css']
})
export class ScientificPapersViewComponent implements OnInit {

  publishedPapers: Array<SubmitionViewDto>;
  searchParams: SearchParams;

  paperToDownload: SubmitionViewDto;
  downloadPopUpDisplayed: boolean;

  constructor(
    private scientificPaperSvc: ScientificPaperService,
    private toastSvc: ToasterService
    ) { 
    this.publishedPapers = [];
    this.downloadPopUpDisplayed = false;
    this.searchParams = new SearchParams();
  }

  ngOnInit() {
    this.scientificPaperSvc.searchScientificPapers(this.searchParams).subscribe(
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

  showPopUp(submition: any): void {
    this.downloadPopUpDisplayed = true;
    this.paperToDownload = submition;
  }

}
