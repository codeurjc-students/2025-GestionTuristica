package com.urjc.plushotel.utils;

public final class EndpointConstants {

    private EndpointConstants() {
    }

    public static class HotelsEndpoints {
        private HotelsEndpoints() {
        }

        public static final String HOTELS_BASE_URL = "/hotels";
        public static final String HOTELS_SLUG_URL = "/hotels/{slug}";
    }

    public static class ReservationsEndpoints {
        private ReservationsEndpoints() {
        }

        public static final String RESERVATIONS_BASE_URL = "/reservations";
        public static final String RESERVATIONS_IDENTIFIER_URL = "/reservations/{reservationIdentifier}";
        public static final String RESERVATIONS_CREATE_URL = "/reservations/{roomId}/reserve";
        public static final String RESERVED_DATES_BY_ROOM_ID = "/reservations/{roomId}/reserved-dates";
    }

    public static class AuthorizationEndpoints {
        private AuthorizationEndpoints() {
        }

        public static final String REGISTER_URL = "/register";
        public static final String LOGIN_URL = "/login";
    }

    public final class RoomsEndpoints {
        private RoomsEndpoints() {
        }

        public static final String ROOMS_ID_URL = "/rooms/{roomId}";
    }
}