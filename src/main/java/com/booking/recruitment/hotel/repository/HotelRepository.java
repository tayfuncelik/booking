package com.booking.recruitment.hotel.repository;

import com.booking.recruitment.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import java.awt.print.Pageable;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findByIdAndDeletedIsFalse(Long id);

    List<Hotel> findAllByDeletedIsFalse();

    String HAVERSINE_FORMULA = "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) *" +
            " cos(radians(h.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(h.latitude))))";
    @Query("SELECT h FROM Hotel h WHERE " + HAVERSINE_FORMULA + " < :distance ORDER BY "+ HAVERSINE_FORMULA + " DESC")
    List<Hotel> findHotelWithInDistance(@Param("latitude") double latitude,
                                         @Param("longitude") double longitude,
                                         @Param("distance") double distanceWithInKM);
}
