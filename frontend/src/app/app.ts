import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HotelListComponent } from "./component/hotel-list.component";

@Component({
  selector: 'app-root',
  imports: [HotelListComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
}
