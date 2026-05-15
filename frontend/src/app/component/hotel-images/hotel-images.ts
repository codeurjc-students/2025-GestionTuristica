import { Room } from './../../services/room.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ImagePreview, ImageService } from '../../services/image-service';
import { FormsModule } from '@angular/forms';
import { RoomService } from '../../services/room.service';
import { CdkDrag, CdkDragDrop, CdkDropList, CdkDropListGroup, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
    selector: 'app-hotel-images',
    imports: [FormsModule, CdkDropList, CdkDrag, CdkDropListGroup],
    templateUrl: './hotel-images.html',
    styleUrl: './hotel-images.scss',
})
export class HotelImages implements OnInit {

    private hotelSlug!: string | null;
    selectedFiles: File[] = [];

    hotelImages: ImagePreview[] = [];

    roomImages: {
        [roomId: number]: ImagePreview[]; 
    } = {};

    rooms: Room[] = [];

    constructor(private readonly activatedRoute: ActivatedRoute,
        private readonly imageService: ImageService,
        private readonly roomService: RoomService,
        private readonly router: Router
    ) { }

    ngOnInit(): void {
        this.hotelSlug = this.activatedRoute.snapshot.paramMap.get('slug');
        if(this.hotelSlug) {
            this.roomService.getRoomsByHotelSlug(this.hotelSlug).subscribe({
                next: (data) => {
                    this.rooms = data;

                    for(const room of this.rooms){
                        this.roomImages[room.id] = [];
                    };
                },
                error: (err) => console.error(err)
            });
        }
        
    }

    onFilesSelected(event: any) {
        const input = event.target as HTMLInputElement;

        const files = Array.from(input.files || []);

        for(const file of  files){
            this.hotelImages.push({
                file,
                type: 'HOTEL',
                previewUrl: URL.createObjectURL(file),
                position: 0
            });
        };

        this.updatePositions(this.hotelImages);
    }

    updatePositions(images: ImagePreview[]) {

        for(const [index, img] of images.entries()) {
            img.position = index;
        }
    }

    drop(event: CdkDragDrop<ImagePreview[]>) {
        
        if(event.previousContainer === event.container) {
            moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
            this.updatePositions(event.container.data);
        } else {
            transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
            this.updatePositions(event.previousContainer.data);
            this.updatePositions(event.container.data);
            this.updateType(event.container.data[event.currentIndex], event.container.id);
        }
    }

    uploadImages() {
        for(const image of this.hotelImages) {

            const formData = new FormData();
            formData.append('file', image.file);
            formData.append('hotelSlug', this.hotelSlug || '');
            if(this.hotelSlug) {
                this.imageService.uploadHotelImage(formData, this.hotelSlug, image.position).subscribe();
            }
        };

        for(const roomList of Object.values(this.roomImages)) {
            for(const image of roomList) {
                const formData = new FormData();
                formData.append('file', image.file);
                formData.append('hotelSlug', this.hotelSlug || '');
                if(image.roomId) {
                    formData.append('roomId', image.roomId.toString());
                    this.imageService.uploadRoomImage(formData, image.roomId, image.position).subscribe();
                }
            }
        }

        this.router.navigate(['/']);
    }

    updateType(image: ImagePreview, listId: string) {
        if(listId.startsWith('hotel')) {
            image.type = 'HOTEL';
            image.roomId = undefined;
        } else {
            image.type = 'ROOM';
            const roomId = Number(listId.replace('room-', ''));
            image.roomId = roomId;
        }
    }

    removeImage(list: ImagePreview[], image: ImagePreview) {

        const imageIndex = list.indexOf(image);

        URL.revokeObjectURL(image.previewUrl)

        list.splice(imageIndex, 1);

        this.updatePositions(list);
    }
}



