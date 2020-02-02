import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';

@Component({
  selector: 'div [app-documents]',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
})
export class DocumentsComponent implements OnInit {

  @ViewChild('searchHolder', {static: false}) searchHolder: ElementRef;
  @ViewChild('searchType', {static: false}) searchType: ElementRef;

  private sparqlResult: any[] = [];

  private apiTimeout = null;

  constructor(private scientificPapersService: ScientificPaperService) { }

  ngOnInit() {
  }

  //TODO This will be modified in future once we implement document retrieval
  search(): void {
    clearTimeout(this.apiTimeout);
    this.apiTimeout = setTimeout(() => {
      this.completeSearch();
    }, 200);

    
  }

  completeSearch(): void {
    var select = <HTMLSelectElement> this.searchType.nativeElement;
    var input = <HTMLInputElement> this.searchHolder.nativeElement;
    if (select.value == 'authors') {
      this.scientificPapersService.searchByAuthorsMetadata(input.value.toLowerCase()).subscribe(
        data => {
          var newData = []
          for (var author in data.result) {
            var titles = []
            for (var i = 0; i < data.result[author].length; i++) {
              titles.push(data.result[author][i]);
            }
            newData.push({
              author: author,
              titles: titles
              
            });
          }
          this.sparqlResult = newData;
          console.log(this.sparqlResult);
        },
        error => {
          console.log(error);
        }
      )
    }
  }

}
