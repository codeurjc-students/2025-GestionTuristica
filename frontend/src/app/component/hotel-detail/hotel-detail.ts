import { Component, OnInit } from '@angular/core';
import { Hotel, HotelService } from '../../services/hotel.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { RoomService } from '../../services/room.service';

@Component({
  selector: 'app-hotel-detail',
  imports: [
    RouterModule
  ],
  templateUrl: './hotel-detail.html',
})
export class HotelDetail implements OnInit {
  slug!: string;
  hotel: Hotel = {
    id: 0,
    name: '',
    description: '',
    country: '',
    city: '',
    address: '',
    stars: 0,
    slug: '',
    rooms: [],
    averageRating: 0
  };

  constructor(
    private readonly route: ActivatedRoute,
    private readonly hotelService: HotelService,
    private readonly roomService: RoomService,
    private readonly router: Router
  ){};

  ngOnInit() {
    this.loadHotel();
  }

  loadHotel() {
    this.slug = String(this.route.snapshot.paramMap.get('slug'));

    this.hotelService.getHotelBySlug(this.slug).subscribe({
      next: (data) => {
        this.hotel = data;
        this.loadRooms();
      }
    });

    this.loadRooms();
  }

  loadRooms() {
      this.roomService.getRoomsByHotelId(this.hotel.id).subscribe({
      next: (data) => {
        this.hotel.rooms = data;
      },
      error: (err) => console.error(err)
      });
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
