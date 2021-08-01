package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
    private final HotelRepository hotelRepository;

    @Autowired
    DefaultHotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<Hotel> getHotelsByCity(Long cityId) {
        return hotelRepository.findAllByDeletedIsFalse().stream()
                .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Hotel createNewHotel(Hotel hotel) {
        if (hotel.getId() != null) {
            throw new BadRequestException("The ID must not be provided when creating a new Hotel");
        }

        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotelsById(Long hotelId) {
        Hotel result = hotelRepository.findByIdAndDeletedIsFalse(hotelId);
        return result;
    }

    @Override
    public void deleteById(Long id) {
        Hotel hotel = getHotelsById(id);
        hotel.setDeleted(true);
        hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> findCloses3Hotels(Long id) {
        Hotel hotel = getHotelsById(id);
        List<Hotel> closes3Hotel = hotelRepository.findHotelWithInDistance(hotel.getLatitude(), hotel.getLongitude(), 5);
        return closes3Hotel.stream().limit(3).collect(Collectors.toList());
    }
}