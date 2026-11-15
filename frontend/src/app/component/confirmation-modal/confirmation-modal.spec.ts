import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationModal, ConfirmationModalData } from './confirmation-modal';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

describe('ConfirmationModal', () => {
  let component: ConfirmationModal;
  let fixture: ComponentFixture<ConfirmationModal>;
  let dialogRefMock: any;

  const dialogDataMock: ConfirmationModalData = {
    title: 'Title',
    message: 'Message',
    type: 'confirmation',
    confirmText: 'Confirm',
    cancelText: 'Cancel'
  };

  beforeEach(async () => {

    dialogRefMock = {
      close: jasmine.createSpy('close')
    }

    await TestBed.configureTestingModule({
      imports: [ConfirmationModal],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: dialogDataMock },
        { provide: MatDialogRef, useValue: dialogRefMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ConfirmationModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog when confirm is called', () => {
    component.confirm();

    expect(dialogRefMock.close).toHaveBeenCalledWith(true);
  });

  it('should close dialog when cancel is called', () => {
    component.cancel();

    expect(dialogRefMock.close).toHaveBeenCalledWith(false);
  });
});
