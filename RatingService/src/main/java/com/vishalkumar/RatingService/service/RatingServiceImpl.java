package com.vishalkumar.RatingService.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vishalkumar.RatingService.entities.Rating;
import com.vishalkumar.RatingService.repository.RatingRepository;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating create(Rating rating) {
        String ratingId = UUID.randomUUID().toString();
        rating.setRatingId(ratingId);
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingByUserId(String userId) {
        return ratingRepository.findByUserId(userId);

    }

    @Override
    public List<Rating> getRatingByHotelId(String hotelId) {
        System.out.println(hotelId);
        return ratingRepository.findByHotelId(hotelId);
    }

    @Override
    public void deleteRating(String ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new RuntimeException("Rating with ID " + ratingId + " not found");
        }
        ratingRepository.deleteById(ratingId);
    }

    @Override
    public Rating updateRating(String ratingId, Rating updatedRating) {
        Rating existingRating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating with ID " + ratingId + " not found"));

        // Update fields
        existingRating.setRating(updatedRating.getRating());
        existingRating.setFeedback(updatedRating.getFeedback());
        // Optionally update hotelId or userId only if needed
        // existingRating.setHotelId(updatedRating.getHotelId());
        // existingRating.setUserId(updatedRating.getUserId());

        return ratingRepository.save(existingRating);
    }
}
