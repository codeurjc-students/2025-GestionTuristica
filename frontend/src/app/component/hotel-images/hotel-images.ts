import { Room } from './../../services/room.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { EditableImage, ImageService } from '../../services/image-service';
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

    private hotelSlug!: string;
    selectedFiles: File[] = [];

    hotelImages: EditableImage[] = [];

    roomImages: {
        [roomId: number]: EditableImage[]; 
    } = {};

    rooms: Room[] = [];

    imagesToDelete: number[] = [];

    constructor(private readonly activatedRoute: ActivatedRoute,
        private readonly imageService: ImageService,
        private readonly roomService: RoomService,
        private readonly router: Router
    ) { }

    ngOnInit(): void {
        this.hotelSlug = String(this.activatedRoute.snapshot.paramMap.get('slug'));
        if(this.hotelSlug) {
            this.roomService.getRoomsByHotelSlug(this.hotelSlug).subscribe({
                next: (data) => {
                    this.rooms = data;

                    for(const room of this.rooms){
                        this.roomImages[room.id!] = [];
                    };

                    this.loadHotelImages();
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

    updatePositions(images: EditableImage[]) {

        for(const [index, img] of images.entries()) {
            img.position = index;
        }
    }

    drop(event: CdkDragDrop<EditableImage[]>) {
        
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

    updateImages() {
        for(const image of this.hotelImages) {
            if(image.file) {
                const formData = new FormData();
                formData.append('file', image.file);
                this.imageService.uploadHotelImage(formData, this.hotelSlug, image.position).subscribe();
            } else if(image.id){
                this.imageService.updateImage(image.id, image).subscribe();
            }
        }

        for(const roomImagesList of Object.values(this.roomImages)) {
            for(const image of roomImagesList) {
                if(image.file) {
                    const formData = new FormData();
                    formData.append('file', image.file);
                    this.imageService.uploadRoomImage(formData, image.roomId!, image.position).subscribe();
                } else if(image.id){
                    this.imageService.updateImage(image.id, image).subscribe();
                }
            }
        }

        for(const imageId of this.imagesToDelete) {
            this.imageService.deleteImage(imageId).subscribe();
        }

        this.router.navigate(['/']);
    }

    updateType(image: EditableImage, listId: string) {
        if(listId.startsWith('hotel')) {
            image.type = 'HOTEL';
            image.roomId = undefined;
        } else {
            image.type = 'ROOM';
            const roomId = Number(listId.replace('room-', ''));
            image.roomId = roomId;
        }
    }

    removeImage(list: EditableImage[], image: EditableImage) {
            const imageIndex = list.indexOf(image);

            if(image.previewUrl) {
                URL.revokeObjectURL(image.previewUrl);
            }

            list.splice(imageIndex, 1);

            this.updatePositions(list);

            if(image.id) {
                this.imagesToDelete.push(image.id);
            }
    }

    loadHotelImages() {
        this.imageService.getImagesByHotelSlug(this.hotelSlug).subscribe(
            (data) => this.hotelImages = data
        );

        for(const room of this.rooms){
            this.imageService.getImagesByRoomId(room.id!).subscribe(
                (data) => this.roomImages[room.id!] = data
            );
        }
    }
}



