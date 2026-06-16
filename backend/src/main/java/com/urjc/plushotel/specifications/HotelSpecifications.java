package com.urjc.plushotel.specifications;

import com.urjc.plushotel.entities.Hotel;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecifications {

    private HotelSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Hotel> hasDeletedFalse() {
        return (root, query, cb) ->
                cb.isFalse(root.get("deleted"));
    }

    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) ->
                country == null ? null : cb.equal(cb.lower(root.get("country")), country.toLowerCase());
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? null : cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Hotel> hasMinStars(Double stars) {
        return (root, query, cb) ->
                stars == null ? null : cb.equal(root.get("stars"), stars);
    }

    public static Specification<Hotel> hasMinRating(Double rating) {
        return (root, query, cb) ->
                rating == null ? null : cb.greaterThanOrEqualTo(root.get("rating"), rating);
    }
}