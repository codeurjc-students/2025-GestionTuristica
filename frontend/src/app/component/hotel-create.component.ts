import { DialogService } from './../services/dialog.service';

import { Component } from "@angular/core";
import { FormsModule, NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { HotelService, HotelRequest, RoomRequest } from "../services/hotel.service";
import { StarRating } from "./star-rating/star-rating";

@Component({
    selector: 'hotel-create',
    standalone: true,
    imports: [FormsModule, StarRating],
    templateUrl: './hotel-create.component.html',
    styleUrl: './hotel-create.component.scss'
})
export class HotelCreateComponent {

    hotelModel: HotelRequest = {
        name: '',
        description: '',
        country: '',
        city: '',
        address: '',
        stars: 1,
        slug: '',
        rooms: [] as RoomRequest[]
    };


    roomModel: RoomRequest = {
        name: '',
        description: '',
        price: 0,
    }

    showForm: boolean = false;

    constructor(private readonly hotelService: HotelService, private readonly router: Router, private readonly dialogService: DialogService) { }

    submit(hotelForm: NgForm) {
        if (hotelForm.invalid) {
            this.dialogService.infoDialog('Error', 'Es necesario rellenar todos los campos');
            return;
        }

        this.hotelModel.slug = this.hotelModel.name.toLowerCase().replaceAll(' ', '-').replaceAll('ñ', 'n').replaceAll(/[^\w-]+/g, '');
        if (this.hotelService.create) {
            this.hotelService.create(this.hotelModel).subscribe({
                next: () => this.dialogService.infoDialog('Éxito', 'El hotel se ha creado correctamente.').subscribe(_ => this.router.navigate(['/hotels/' + this.hotelModel.slug + '/images'])),
                error: () => this.dialogService.infoDialog('Error', 'Ha habido un error creando el hotel')
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
        if (!(this.roomModel.description && this.roomModel.name && this.roomModel.price)) {
            this.dialogService.infoDialog('Error', 'Es necesario rellenar todos los campos para crear la habitación')
            return;
        }

        this.hotelModel.rooms.push(this.roomModel);
        this.resetRoomForm();
        this.toggleRoomFormVisibility();
    }

    removeRoom(room: RoomRequest) {
        this.hotelModel.rooms = this.hotelModel.rooms.filter(r => r !== room);
    }

    resetRoomForm() {
        this.roomModel = {
            name: '',
            description: '',
            price: 0,
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
