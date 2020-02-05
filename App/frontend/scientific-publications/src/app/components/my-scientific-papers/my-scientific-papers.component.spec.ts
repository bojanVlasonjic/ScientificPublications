import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyScientificPapersComponent } from './my-scientific-papers.component';

describe('MyScientificPapersComponent', () => {
  let component: MyScientificPapersComponent;
  let fixture: ComponentFixture<MyScientificPapersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyScientificPapersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyScientificPapersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
