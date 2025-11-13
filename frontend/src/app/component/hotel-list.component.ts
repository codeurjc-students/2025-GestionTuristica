import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Hotel, HotelService } from "../services/hotel.service";


@Component({
    selector: 'hotel-list',
    standalone: true,
    imports: [ CommonModule ],
    templateUrl: './hotel-list.component.html'
})
export class HotelListComponent implements OnInit{

    hotels: Hotel[] = []

    constructor(private hotelService: HotelService){}

    ngOnInit() {
        this.hotelService.getHotels().subscribe({
            next: (data) => this.hotels = data,
            error: (err) => console.error(err)
        });
    }
}