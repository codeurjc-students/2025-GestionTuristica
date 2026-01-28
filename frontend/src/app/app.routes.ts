import { Routes } from '@angular/router';
import { HotelListComponent } from './component/hotel-list.component';
import { HotelCreateComponent } from './component/hotel-create.component';
import { HotelEdit } from './component/hotel-edit.component';
import { HotelDetail } from './component/hotel-detail/hotel-detail';
import { RoomDetail } from './component/room-detail/room-detail';
import { ReservationList } from './component/reservation-list/reservation-list';
import { ReservationEdit } from './component/reservation-edit/reservation-edit';
import { Register } from './component/register/register';
import { Login } from './component/login/login';

export const routes: Routes = [
    { path: 'hotels', component: HotelListComponent },
    { path: 'hotels/create', component: HotelCreateComponent },
    { path: 'hotels/edit/:slug', component: HotelEdit },
    { path: 'hotels/detail/:slug', component: HotelDetail },
    { path: 'hotels/:slug/rooms/:roomId/detail', component: RoomDetail },
    { path: 'reservations', component: ReservationList },
    { path: 'reservations/:reservationIdentifier', component: ReservationEdit },
    { path: 'register', component: Register},
    { path: 'login', component: Login }
];
