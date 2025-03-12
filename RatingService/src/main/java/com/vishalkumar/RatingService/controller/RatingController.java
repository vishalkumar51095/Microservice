package com.vishalkumar.RatingService.controller;
import com.vishalkumar.RatingService.entities.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vishalkumar.RatingService.service.RatingService;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    //creating rating
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Rating rating){
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(rating));
    }

    //get all
    @GetMapping
    public ResponseEntity<?> getRatings(){
        return ResponseEntity.ok(ratingService.getRatings());
    }

    //get by user id
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getRatingsByUserId(@PathVariable  String userId){
        return ResponseEntity.ok(ratingService.getRatingByUserId(userId));
    }


    @GetMapping("hotels/{hotelId}")
    public ResponseEntity<?> getRatingsByHotelId(@PathVariable String hotelId) {
        System.out.println("hotelId: " + hotelId);
        return ResponseEntity.ok(ratingService.getRatingByHotelId(hotelId));
    }

}
