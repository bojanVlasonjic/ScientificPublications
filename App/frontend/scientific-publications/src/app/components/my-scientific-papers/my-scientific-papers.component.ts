import { Component, OnInit } from '@angular/core';
import { AuthorSubmitionDTO } from 'src/app/models/submitions/author-submition-dto.model';
import { SubmitionService } from 'src/app/services/submition.service';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-my-scientific-papers',
  templateUrl: './my-scientific-papers.component.html',
  styleUrls: ['./my-scientific-papers.component.css']
})
export class MyScientificPapersComponent implements OnInit {

  submitions: Array<AuthorSubmitionDTO>;

  constructor(private submitionSvc: SubmitionService , private toastSvc: ToasterService) {
    this.submitions = [];
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

}
