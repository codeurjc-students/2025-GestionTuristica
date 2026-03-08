import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewEdit } from './review-edit';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { ReviewService } from '../../services/review.service';

describe('ReviewEdit', () => {
  let component: ReviewEdit;
  let fixture: ComponentFixture<ReviewEdit>;
  let activatedRouteMock: any;
  let reviewServiceMock: any;
  let routerMock: any;

  const mockReview = {
    userEmail: 'john@example.com',
    reservationIdentifier: 'RSV-123',
    roomName: 'Room1',
    message: 'Test message',
    rating: 4,
    creationTime: '2026-03-08'
  };

  beforeEach(async () => {

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('RSV-123')
        }
      }
    };

    reviewServiceMock = {
      getReviewByReservationIdentifier: jasmine.createSpy('getReviewByReservationIdentifier').and.returnValue(of(mockReview)),
      updateReview: jasmine.createSpy('updateReview').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    }

    await TestBed.configureTestingModule({
      imports: [ReviewEdit],
      providers: [
        { provide: ReviewService, useValue: reviewServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: Router, useValue: routerMock },
        provideHttpClient()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load review on init', () => {
    expect(reviewServiceMock.getReviewByReservationIdentifier).toHaveBeenCalledWith('RSV-123');
    expect(component.review).toEqual(mockReview);
  });

  it('should update review and navigate to reviews page', () => {
    component.review.message = "Updated message";
    component.review.rating = 5;
    component.updateReview();

    expect(reviewServiceMock.updateReview).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews']);
  });
});
