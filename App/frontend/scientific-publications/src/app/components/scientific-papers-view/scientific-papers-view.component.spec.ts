import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScientificPapersViewComponent } from './scientific-papers-view.component';

describe('ScientificPapersViewComponent', () => {
  let component: ScientificPapersViewComponent;
  let fixture: ComponentFixture<ScientificPapersViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScientificPapersViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScientificPapersViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
