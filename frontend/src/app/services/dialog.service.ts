import { ConfirmationModalData } from './../component/confirmation-modal/confirmation-modal';
import { inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationModal } from '../component/confirmation-modal/confirmation-modal';

@Injectable({
  providedIn: 'root',
})
export class DialogService {

  private readonly dialog = inject(MatDialog)

  confirmationDialog(title: string, message: string, confirmText: string, cancelText: string) {
    return this.dialog.open<ConfirmationModal, ConfirmationModalData, boolean>(
      ConfirmationModal,
      {
        width: '50vw',
        data: { title, message, type: 'confirmation', confirmText, cancelText }
      }
    ).afterClosed();
  }

  infoDialog(title: string, message: string) {
    return this.dialog.open<ConfirmationModal, ConfirmationModalData, boolean>(
      ConfirmationModal,
      {
        width: '50vw',
        data: { title, message, type: 'info' }
      }
    ).afterClosed();
  }
}
