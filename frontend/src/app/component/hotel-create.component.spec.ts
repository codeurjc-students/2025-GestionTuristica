import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HotelCreateComponent } from "./hotel-create.component";
import { HotelService, Room } from "../services/hotel.service";
import { Router } from "@angular/router";
import {of} from 'rxjs';

describe('HotelCreateComponent', () => {
    let component: HotelCreateComponent;
    let fixture: ComponentFixture<HotelCreateComponent>;
    let hotelServiceMock: any;
    let routerMock: any;

    beforeEach(async () => {
        hotelServiceMock = {
            create: jasmine.createSpy('create').and.returnValue(of({}))
        };

        routerMock = {
            navigate: jasmine.createSpy('navigate')
        };

        await TestBed.configureTestingModule({
            imports: [HotelCreateComponent],
            providers:[
                {provide: HotelService, useValue: hotelServiceMock},
                {provide: Router, useValue: routerMock}
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(HotelCreateComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('Create component', () => {
        expect(component).toBeTruthy();
    });

    it('Slug should be generated successfully when submitting', () => {
        component.hotelModel.name = "Hotel España Test";
        component.submit();
        expect(component.hotelModel.slug).toBe('hotel-espana-test');
    });

    it('Submit should call hotelService create and navigate to main page', () => {
        component.hotelModel.name = 'Hotel Test';
        component.submit();
        expect(hotelServiceMock.create).toHaveBeenCalled();
        expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
    });

    it('Room should be added when addRoom is called', () => {
        const room: Room = {name: 'Room 1', description: '', price: 30, available: true};
        component.roomModel = room;
        component.addRoom();
        expect(component.hotelModel.rooms.length).toBe(1);
        expect(component.hotelModel.rooms[0]).toEqual(room);
    });

    it('Room should be removed when removeRoom is called', () => {
        const room1: Room = {name: 'Room 1', description: '', price: 30, available: true};
        const room2: Room = {name: 'Room 2', description: '', price: 50, available: true};
        component.hotelModel.rooms = [room1, room2];
        component.removeRoom(room1);
        expect(component.hotelModel.rooms.length).toBe(1);
        expect(component.hotelModel.rooms[0]).toEqual(room2);
    });

    it('The value of showForm should be toggled when toggleRoomFormVisibility is called', () => {
        expect(component.showForm).toBeFalse();
        component.toggleRoomFormVisibility();
        expect(component.showForm).toBeTrue();
    });

    it('Cancelling the hotel creation should navigate to the main page', () => {

    });
});