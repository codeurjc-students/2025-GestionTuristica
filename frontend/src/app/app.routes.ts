import { Routes } from '@angular/router';
import { HotelListComponent } from './component/hotel-list.component';
import { HotelCreateComponent } from './component/hotel-create.component';

export const routes: Routes = [
    { path: '', component: HotelListComponent },
    { path: 'hotels/create', component: HotelCreateComponent }
];
