package com.vishalkumar.RatingService.controller;
import com.vishalkumar.RatingService.entities.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable String ratingId) {
        try {
            ratingService.deleteRating(ratingId);
            return ResponseEntity.ok("Rating deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Rating> updateRating(@PathVariable String ratingId, @RequestBody Rating updatedRating) {
        try {
            Rating rating = ratingService.updateRating(ratingId, updatedRating);
            return ResponseEntity.ok(rating);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(null);
        }
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
