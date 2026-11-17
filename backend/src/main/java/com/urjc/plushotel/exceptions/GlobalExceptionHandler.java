package com.urjc.plushotel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //NOT FOUND

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<Void> handleHotelNotFoundException(HotelNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Void> handleReservationNotFoundException(ReservationNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Void> handleImageNotFoundException(ImageNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @ExceptionHandler(ReservationChangeRequestNotFoundException.class)
    public ResponseEntity<Void> handleReservationChangeRequestNotFoundException(ReservationChangeRequestNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Void> handleReviewNotFoundException(ReviewNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Void> handleRoomNotFoundException(RoomNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // BAD REQUEST

    @ExceptionHandler(InvalidReservationRangeException.class)
    public ResponseEntity<Void> handleInvalidReservationRangeException(InvalidReservationRangeException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NullModificationDatesException.class)
    public ResponseEntity<Void> handleNullModificationDatesException(NullModificationDatesException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // INTERNAL SERVER ERROR

    @ExceptionHandler(MinioErrorException.class)
    public ResponseEntity<Void> handleMinioErrorException(MinioErrorException e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // CONFLICT

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Void> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


}
