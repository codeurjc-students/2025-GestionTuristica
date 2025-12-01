import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { HotelService, Hotel, Room } from "../services/hotel.service";

@Component({
    selector: 'hotel-create',
    standalone: true,
    imports: [ CommonModule, FormsModule ],
    templateUrl: './hotel-create.component.html'
})
export class HotelCreateComponent {

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


    roomModel: Room = {
        name: '',
        description: '',
        price: 0,
        available: true,
    }

    showForm: boolean = false;

    constructor(private readonly hotelService: HotelService, private readonly router: Router) {}

    submit() {
        this.hotelModel.slug = this.hotelModel.name.toLowerCase().replaceAll(' ', '-').replaceAll('ñ', 'n').replaceAll(/[^\w-]+/g, '');
        if (this.hotelService.create) {
            this.hotelService.create(this.hotelModel).subscribe({
                next: () => void this.router.navigate(['/']),
                error: (err) => console.error(err)
            });
        } else {
            console.log('Form value:', this.hotelModel);
            this.router.navigate(['/']);
        }
    }

    toggleRoomFormVisibility() {
        this.showForm = !this.showForm;
    }

    addRoom() {
        this.hotelModel.rooms.push(this.roomModel);
        this.resetRoomForm();
        this.toggleRoomFormVisibility();
    }

    removeRoom(room: Room) {
        this.hotelModel.rooms = this.hotelModel.rooms.filter(r => r !== room);
    }

    resetRoomForm() {
        this.roomModel = {
            name: '',
            description: '',
            price: 0,
            available: true,
        };
    }

    cancelRoomCreation() {
        this.resetRoomForm();
        this.toggleRoomFormVisibility();
    }

    cancel() {
        this.router.navigate(['/']);
    }
}
