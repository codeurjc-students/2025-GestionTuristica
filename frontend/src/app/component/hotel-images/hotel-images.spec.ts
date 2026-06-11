import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HotelImages } from './hotel-images';
import { Room } from '../../services/hotel.service';
import { EditableImage, ImageService } from '../../services/image-service';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../services/room.service';

describe('HotelImages', () => {
  let component: HotelImages;
  let fixture: ComponentFixture<HotelImages>;
  let imageServiceMock: any;
  let roomServiceMock: any;
  let routerMock: any;
  let activatedRouteMock: any;

  const mockRooms: Room[] = [
    {
      id: 1,
      name: 'room1',
      description: 'room1 description',
      price: 30
    },
    {
      id: 2,
      name: 'room2',
      description: 'room2 description',
      price: 20
    }
  ];

  const mockHotelImages: EditableImage[] = [
    {
      id: 1,
      url: 'url.example/image1',
      type: 'HOTEL',
      position: 0
    },
    {
      id: 2,
      url: 'url.example/image2',
      type: 'HOTEL',
      position: 1
    }
  ];

  const mockRoom1Images: EditableImage[] = [
    {
      id: 3,
      url: 'url.example/room1Image1',
      type: 'ROOM',
      hotelId: 1,
      roomId: 1,
      position: 0
    },
    {
      id: 4,
      url: 'url.example/room1Image2',
      type: 'ROOM',
      hotelId: 1,
      roomId: 1,
      position: 1
    }
  ];

  const mockRoom2Images: EditableImage[] = [
    {
      id: 5,
      url: 'url.example/room2Image1',
      type: 'ROOM',
      hotelId: 1,
      roomId: 2,
      position: 0
    },
    {
      id: 6,
      url: 'url.example/room2Image2',
      type: 'ROOM',
      hotelId: 1,
      roomId: 2,
      position: 1
    }
  ];

  beforeEach(async () => {

    roomServiceMock = {
      getNonPaginatedRoomsByHotelSlug: jasmine.createSpy('getNonPaginatedRoomsByHotelSlug').and.returnValue(of(mockRooms))
    };

    imageServiceMock = {
      getImagesByHotelSlug: jasmine.createSpy('getImagesByHotelSlug').and.returnValue(of(mockHotelImages)),
      getImagesByRoomId: jasmine.createSpy('getImagesByRoomId').and.callFake((roomId: number) => {
        if(roomId === 1) {
          return of(mockRoom1Images);
        } else {
          return of(mockRoom2Images);
        }
      }),
      updateImage: jasmine.createSpy('updateImage').and.returnValue(of({})),
      uploadHotelImage: jasmine.createSpy('uploadHotelImage').and.returnValue(of({})),
      uploadRoomImage: jasmine.createSpy('uploadRoomImage').and.returnValue(of({})),
      deleteImage: jasmine.createSpy('deleteImage').and.returnValue(of({}))
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('hotel-test')
        }
      }
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    await TestBed.configureTestingModule({
      imports: [HotelImages],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: RoomService, useValue: roomServiceMock },
        { provide: ImageService, useValue: imageServiceMock },
        { provide: Router, useValue: routerMock },
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HotelImages);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load images on init', () => {
    expect(component.rooms).toEqual(mockRooms);

    expect(component.hotelImages).toEqual(mockHotelImages);

    expect(component.roomImages[1]).toEqual(mockRoom1Images);

    expect(component.roomImages[2]).toEqual(mockRoom2Images);
  });

  it('should update image position', () => {
    const images: EditableImage[] = [
      {
      id: 1,
      url: 'url.example/image1',
      type: 'HOTEL',
      position: 3
    },
    {
      id: 2,
      url: 'url.example/image2',
      type: 'HOTEL',
      position: 2
    }
  ];

    component.updatePositions(images);

    expect(images[0].position).toBe(0);
    expect(images[1].position).toBe(1);
  });

  it('should update type of image', () => {
    const convertingToRoom: EditableImage = {
      id: 1,
      url: 'url.example/image1',
      type: 'HOTEL',
      position: 0
    };

    const convertingToHotel: EditableImage = {
      id: 1,
      url: 'url.example/image2',
      type: 'ROOM',
      position: 0
    };

    component.updateType(convertingToHotel, 'hotel');
    component.updateType(convertingToRoom, 'room-1');

    expect(convertingToHotel.type).toBe('HOTEL');
    expect(convertingToHotel.roomId).toBeUndefined();
    expect(convertingToRoom.type).toBe('ROOM');
    expect(convertingToRoom.roomId).toBe(1);
  });

  it('should remove image', () => {
    const images: EditableImage[] = [
      {
        id: 1,
        url: 'url.example/image1',
        type: 'HOTEL',
        position: 0
      },
      {
        id: 2,
        url: 'url.example/image2',
        type: 'HOTEL',
        position: 1
      }
    ];

    component.removeImage(images, images[0]);

    expect(images.length).toBe(1);
    expect(images[0].id).toBe(2);
    expect(component.imagesToDelete).toContain(1);
  });

  it('should send requests to update images and navigate to main page', () => {
    component.rooms = mockRooms;

    component.hotelImages = [
      {
        id: 1,
        url: 'url.example/image1',
        type: 'HOTEL',
        position: 0
      },
      {
        file: new File([], 'image2.jpg'),
        previewUrl: 'url.example/image2',
        type: 'HOTEL',
        position: 1
      }
    ];

    component.roomImages = {
      1: [
        {
          id: 4,
          url: 'url.example/room1Image1',
          type: 'ROOM',
          hotelId: 1,
          roomId: 2,
          position: 0
        },
        {
          file: new File([], 'image4.jpg'),
          previewUrl: 'url.example/room1Image2',
          type: 'ROOM',
          hotelId: 1,
          roomId: 1,
          position: 1
        }
      ],
      2: [
        {
          id: 6,
          url: 'url.example/room2Image1',
          type: 'ROOM',
          hotelId: 1,
          roomId: 2,
          position: 0
        },
        {
          file: new File([], 'image7.jpg'),
          previewUrl: 'url.example/room2Image2',
          type: 'ROOM',
          hotelId: 1,
          roomId: 2,
          position: 1
        }
      ],
    };

    component.imagesToDelete = [3];


    component.updateImages();
    expect(imageServiceMock.updateImage).toHaveBeenCalledWith(1, component.hotelImages[0]);
    expect(imageServiceMock.uploadHotelImage).toHaveBeenCalledWith(jasmine.any(FormData), 'hotel-test', 1);
    expect(imageServiceMock.updateImage).toHaveBeenCalledWith(4, component.roomImages[1][0]);
    expect(imageServiceMock.updateImage).toHaveBeenCalledWith(6, component.roomImages[2][0]);
    expect(imageServiceMock.uploadRoomImage).toHaveBeenCalledWith(jasmine.any(FormData), 1, 1);
    expect(imageServiceMock.uploadRoomImage).toHaveBeenCalledWith(jasmine.any(FormData), 2, 1);
    expect(imageServiceMock.deleteImage).toHaveBeenCalledWith(3);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});
