import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentValidationComponent } from './document-validation.component';

describe('DocumentValidationComponent', () => {
  let component: DocumentValidationComponent;
  let fixture: ComponentFixture<DocumentValidationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentValidationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentValidationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
