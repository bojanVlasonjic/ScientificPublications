import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubmitionComponent } from './create-submition.component';

describe('CreateSubmitionComponent', () => {
  let component: CreateSubmitionComponent;
  let fixture: ComponentFixture<CreateSubmitionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateSubmitionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateSubmitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
