import { HotelService, Room } from './../services/hotel.service';
import { Component, OnInit } from '@angular/core';
import { Hotel } from '../services/hotel.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'hotel-edit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hotel-edit.component.html',
})
export class HotelEdit implements OnInit{
  slug!: string;
  hotel!: Hotel;

  hotelModel: Hotel = {
    name: '',
    description: '',
    country: '',
    city: '',
    address: '',
    stars: 1,
    slug: '',
    rooms: [] as Room[]
  };

  selectedRoomIndex: number = -1;
  creatingRoom = false;
  showForm = false;
  updatingRoom = false;
  roomModel: Room = { name: '', description: '', price: 0, available: true };

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
        this.hotelModel = this.hotel;
      }

    })
  }

  submit() {
    this.hotelService.updateHotel(this.hotelModel).subscribe({
      next: () => void this.router.navigate(['/']),
      error: (err) => console.error(err)
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }

  toggleRoomFormVisibility() {
    this.showForm = !this.showForm;
    if(!this.showForm){
      this.creatingRoom = false;
      this.updatingRoom = false;
    }
  }

  resetSelectedRoomIndex() {
    this.selectedRoomIndex = -1;
  }

  showRoomUpdateForm() {
    this.updatingRoom = true;
    if(this.showForm) {
      this.cancelRoomCreation();
    }
    this.toggleRoomFormVisibility();
  }

  startRoomCreation() {
    this.creatingRoom = true;
    if(this.showForm) {
      this.cancelRoomUpdate();
    }
    this.toggleRoomFormVisibility();
  }

  startRoomUpdate(room: Room, index: number) {
    this.selectedRoomIndex = index;
    this.showRoomUpdateForm();
    this.roomModel = { ...room };
  }

  addRoom() {
    this.hotelModel.rooms = this.hotelModel.rooms || [];
    this.hotelModel.rooms.push({ ...this.roomModel });
    this.roomModel = { name: '', description: '', price: 0, available: true };
    this.toggleRoomFormVisibility();
  }

  updateRoom() {
    Object.assign(this.hotelModel.rooms[this.selectedRoomIndex], this.roomModel)
    this.roomModel = { name: '', description: '', price: 0, available: true };
    this.toggleRoomFormVisibility();
    this.resetSelectedRoomIndex();
  }

  cancelRoomCreation() {
    this.creatingRoom = false;
    this.roomModel = { name: '', description: '', price: 0, available: true };
    this.toggleRoomFormVisibility();
  }

  cancelRoomUpdate() {
    this.updatingRoom = false;
    this.roomModel = { name: '', description: '', price: 0, available: true };
    this.toggleRoomFormVisibility();
    this.resetSelectedRoomIndex();
  }

  removeRoom(room: Room) {
    this.hotelModel.rooms = this.hotelModel.rooms.filter(r => r !== room);
  }
}
