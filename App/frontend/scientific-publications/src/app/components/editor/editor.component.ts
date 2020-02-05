import { Component, OnInit } from '@angular/core';
import { SubmitionService } from 'src/app/services/submition.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {

  private selectedPaper = null;
  private submitions = [];

  constructor(private submitionService: SubmitionService,
              private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.submitionService.getAllSubmitions().subscribe(
      data => {
        this.submitions = data.results;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

  selectPaper(paper) {
    this.selectedPaper = paper;
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  goToLink(url: string){
    window.open(url, "_blank");
  }

}
