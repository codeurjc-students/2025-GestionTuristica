import { NgClass } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-star-rating',
  imports: [MatIconModule, NgClass],
  templateUrl: './star-rating.html',
  styleUrl: './star-rating.scss',
})
export class StarRating {

  stars = [1, 2, 3, 4, 5];

  @Input() rating?: number;

  @Input() editable: boolean = false;

  @Input() allowClear: boolean = false;

  @Input() allowHalfStars: boolean = true;

  @Output() valueChange = new EventEmitter<number>();

  @Output() clear = new EventEmitter<undefined>();

  hoverRating: number | null = null;

  private get displayedRating() {

    return this.hoverRating ?? this.rating ?? 0;
  }

  isFullStar(star: number) {

    return this.displayedRating >= star;
  }

  isHalfStar(star: number) {

    return this.displayedRating >= star - 0.5 && this.displayedRating < star;
  }

  setRating(event: MouseEvent, star: number) {

    if (!this.editable) {
      return;
    }

    const element = event.target as HTMLElement;

    const rect = element.getBoundingClientRect();

    const clickX = event.clientX - rect.left;

    if (!this.allowHalfStars) {
      this.rating = star;
    } else if (clickX < rect.width / 2) {
      this.rating = star - 0.5;
    } else {
      this.rating = star;
    }

    this.valueChange.emit(this.rating);
  }

  getStarIcon(star: number) {
    if (this.isFullStar(star)) {
      return 'star';
    } else if (this.isHalfStar(star)) {
      return 'star_half';
    } else {
      return 'star_border';
    }
  }

  updateHover(event: MouseEvent, star: number) {

    if (!this.editable) {
      return;
    }

    const element = event.target as HTMLElement;

    const rect = element.getBoundingClientRect();

    const clickX = event.clientX - rect.left;

    if (!this.allowHalfStars) {
      this.hoverRating = star;
    } else if (clickX < rect.width / 2) {
      this.hoverRating = star - 0.5;
    } else {
      this.hoverRating = star;
    }
  }

  clearHover() {

    this.hoverRating = null;
  }

  clearRating() {

    if (!this.editable || !this.allowClear) {
      return;
    }

    this.rating = undefined;

    this.clear.emit(undefined);
  }
}
