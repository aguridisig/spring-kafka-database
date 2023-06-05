import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersSearchComponent } from './users-search.component';

describe('UsersSearchComponent', () => {
  let component: UsersSearchComponent;
  let fixture: ComponentFixture<UsersSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsersSearchComponent]
    });
    fixture = TestBed.createComponent(UsersSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
