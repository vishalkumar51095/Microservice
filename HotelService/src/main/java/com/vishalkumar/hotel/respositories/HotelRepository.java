package com.vishalkumar.hotel.respositories;


import com.vishalkumar.hotel.entites.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, String> {
}
