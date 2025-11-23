import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { HotelService } from "../services/hotel.service";

@Component({
    selector: 'hotel-create',
    standalone: true,
    imports: [ CommonModule, FormsModule ],
    templateUrl: './hotel-create.component.html'
})
export class HotelCreateComponent {

    model = {
        name: '',
        description: '',
        country: '',
        city: '',
        address: '',
        stars: 1,
        slug: ''
    };

    constructor(private readonly hotelService: HotelService, private readonly router: Router) {}

    submit() {
        this.model.slug = this.model.name.toLowerCase().replaceAll(' ', '-').replaceAll('ñ', 'n').replaceAll(/[^\w-]+/g, '');
        if (this.hotelService.create) {
            this.hotelService.create(this.model).subscribe({
                next: () => void this.router.navigate(['/']),
                error: (err) => console.error(err)
            });
        } else {
            console.log('Form value:', this.model);
            this.router.navigate(['/']);
        }
    }

    cancel() {
        this.router.navigate(['/']);
    }
}
