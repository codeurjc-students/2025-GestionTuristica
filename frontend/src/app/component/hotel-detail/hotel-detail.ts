import { Component, OnInit } from '@angular/core';
import { Hotel, HotelService } from '../../services/hotel.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { RoomService } from '../../services/room.service';
import { ImageService, Image } from '../../services/image-service';

@Component({
  selector: 'app-hotel-detail',
  imports: [
    RouterModule
  ],
  templateUrl: './hotel-detail.html',
  styleUrl: './hotel-detail.scss',
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
    averageRating: 0,
    mainImageUrl: ''
  };

  images: Image[] = [];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly hotelService: HotelService,
    private readonly roomService: RoomService,
    private readonly router: Router,
    private readonly imageService: ImageService
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
        this.loadImages();
      }
    });
  }

  loadRooms() {
      this.roomService.getRoomsByHotelSlug(this.hotel.slug).subscribe({
      next: (data) => {
        this.hotel.rooms = data;
      },
      error: (err) => console.error(err)
      });
  }

  loadImages() {
    this.imageService.getImagesByHotelSlug(this.hotel.slug).subscribe({
      next: (data) => {
        this.images = data;
      },
      error: (err) => console.error(err)
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
