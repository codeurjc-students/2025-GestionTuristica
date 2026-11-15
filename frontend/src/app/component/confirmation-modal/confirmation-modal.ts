import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from '@angular/material/dialog'

export interface ConfirmationModalData {
  title: string;
  message: string;
  type: 'info' | 'confirmation';
  confirmText?: string;
  cancelText?: string;
}

@Component({
  selector: 'app-confirmation-modal',
  imports: [MatDialogContent, MatDialogActions],
  templateUrl: './confirmation-modal.html',
  styleUrl: './confirmation-modal.scss',
})
export class ConfirmationModal {

  readonly data = inject(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ConfirmationModal, boolean>);

  confirm() {
    this.dialogRef.close(true);
  }

  cancel() {
    this.dialogRef.close(false);
  }
}
