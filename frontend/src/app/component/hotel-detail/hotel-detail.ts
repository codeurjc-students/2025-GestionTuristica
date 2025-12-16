import { Component, OnInit } from '@angular/core';
import { Hotel, HotelService } from '../../services/hotel.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-hotel-detail',
  imports: [],
  templateUrl: './hotel-detail.html',
})
export class HotelDetail implements OnInit {
  slug!: string;
  hotel!: Hotel;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly hotelService: HotelService,
    private readonly router: Router
  ){};

  ngOnInit() {
    this.slug = String(this.route.snapshot.paramMap.get('slug'));

    this.hotelService.getHotelBySlug(this.slug).subscribe({
      next: (data) => {
        this.hotel = data;
      }
    })
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
