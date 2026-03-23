import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewCreate } from './review-create';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { ReviewService } from '../../services/review.service';

describe('ReviewCreate', () => {
  let component: ReviewCreate;
  let fixture: ComponentFixture<ReviewCreate>;
  let activatedRouteMock: any;
  let reviewServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('RSV-123')
        }
      }
    };

    reviewServiceMock = {
      createReview: jasmine.createSpy('createReview').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    await TestBed.configureTestingModule({
      imports: [ReviewCreate],
      providers: [
        { provide: ReviewService, useValue: reviewServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: Router, useValue: routerMock },
        provideHttpClient()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create review and navigate to reviews page', () => {
    component.message = "Test message";
    component.rating = 4.5;

    component.createReview();

    expect(reviewServiceMock.createReview).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews']);
  });
});
