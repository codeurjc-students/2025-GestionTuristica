import { roleGuard } from './role.guard';
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
import { authGuard } from './auth.guard';
import { Requests } from './component/requests/requests';

export const routes: Routes = [
    { path: 'hotels', component: HotelListComponent },
    { path: 'hotels/create', component: HotelCreateComponent, canActivate: [roleGuard], data: {roles: ['ROLE_ADMIN']} },
    { path: 'hotels/edit/:slug', component: HotelEdit, canActivate: [roleGuard], data: {roles: ['ROLE_ADMIN']} },
    { path: 'hotels/detail/:slug', component: HotelDetail },
    { path: 'hotels/:slug/rooms/:roomId/detail', component: RoomDetail },
    { path: 'reservations', component: ReservationList, canActivate: [authGuard], data: {roles: ['ROLE_USER', 'ROLE_ADMIN']} },
    { path: 'reservations/:reservationIdentifier', component: ReservationEdit, canActivate: [authGuard] },
    { path: 'register', component: Register},
    { path: 'login', component: Login },
    { path: 'requests', component: Requests, canActivate: [roleGuard], data: {roles: ['ROLE_ADMIN']} }
];
