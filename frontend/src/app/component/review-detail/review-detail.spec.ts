import { ActivatedRoute, Router } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewDetail } from './review-detail';
import { provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { ReviewService } from '../../services/review.service';

describe('ReviewDetail', () => {
  let component: ReviewDetail;
  let fixture: ComponentFixture<ReviewDetail>;
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
      deleteReview: jasmine.createSpy('deleteReview').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    await TestBed.configureTestingModule({
      imports: [ReviewDetail],
      providers: [
        { provide: ReviewService, useValue: reviewServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: Router, useValue: routerMock },
        provideHttpClient()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewDetail);
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

  it('updateReview should navigate to edit page', () => {
    component.updateReview();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews/edit', 'RSV-123']);
  });

  it('deleteReview should call reviewService delete and navigate to reviews page', () => {
    component.deleteReview();
    expect(reviewServiceMock.deleteReview).toHaveBeenCalledWith('RSV-123');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews']);
  });
});
