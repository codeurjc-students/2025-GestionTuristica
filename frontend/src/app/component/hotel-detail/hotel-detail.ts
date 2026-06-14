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
    rating: 0,
    mainImageUrl: ''
  };

  images: Image[] = [];

  pageNumber: number = 0;
  firstPage!: boolean;
  lastPage!: boolean;
  numberOfRooms!: number;
  numberOfPages!: number;

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
        this.loadRooms(this.pageNumber);
        this.loadImages();
      }
    });
  }

  loadRooms(page: number) {
      this.roomService.getRoomsByHotelSlug(this.hotel.slug, page).subscribe({
      next: (data) => {
        this.hotel.rooms = data.content;
        this.pageNumber = data.number;
        this.firstPage = data.first;
        this.lastPage = data.last;
        this.numberOfRooms = data.totalElements;
        this.numberOfPages = data.totalPages;
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

  nextPage() {
      if(!this.lastPage) {
          this.pageNumber++;
          this.loadRooms(this.pageNumber);
      }
  }

  previousPage() {
      if(!this.firstPage) {
          this.pageNumber--;
          this.loadRooms(this.pageNumber);
      }
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
