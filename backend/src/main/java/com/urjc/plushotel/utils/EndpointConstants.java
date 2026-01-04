package com.urjc.plushotel.utils;

public final class EndpointConstants {

    private EndpointConstants() {
    }

    public static class ReservationsEndpoints {
        private ReservationsEndpoints() {
        }

        public static final String RESERVATIONS_CREATE_URL = "/reservations/{roomId}/reserve";
        public static final String RESERVED_DATES_BY_ROOM_ID = "/reservations/{roomId}/reserved-dates";
    }

    public final class RoomsEndpoints {
        private RoomsEndpoints() {
        }

        public static final String ROOMS_ID_URL = "/rooms/{roomId}";
    }
}
