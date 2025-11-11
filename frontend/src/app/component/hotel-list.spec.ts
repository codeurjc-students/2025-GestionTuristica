import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of } from 'rxjs';
import { HotelListComponent } from "./hotel-list.component";
import { HotelService } from "../services/hotel.service";

describe('HotelListComponent', () => {
    let component: HotelListComponent;
    let fixture: ComponentFixture<HotelListComponent>;
    let mockService: any;

    beforeEach(async () => {
        mockService = {
            getHotels: jasmine.createSpy('getHotels').and.returnValue(of([]))
        };

        await TestBed.configureTestingModule({
            imports: [HotelListComponent],
            providers: [
                {provide: HotelService, useValue: mockService}
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(HotelListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges;
    });

    it('create component', () => {
        expect(component).toBeTruthy();
    });

    it('call onInit', () => {
        component.ngOnInit();
    });

    it('empty initially', () => {
       expect(component.hotels.length).toBe(0); 
    });
})