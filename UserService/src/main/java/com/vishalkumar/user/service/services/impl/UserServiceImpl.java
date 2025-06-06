package com.vishalkumar.user.service.services.impl;

import com.vishalkumar.user.service.entities.Hotel;
import com.vishalkumar.user.service.entities.Rating;
import com.vishalkumar.user.service.entities.User;
import com.vishalkumar.user.service.exceptions.ResourceNotFoundException;
import com.vishalkumar.user.service.external.service.HotelService;
import com.vishalkumar.user.service.repositories.UserRepository;
import com.vishalkumar.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelService hotelService;
    @Autowired
    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    @Override
    public User saveUser(User user) {
        //generate unique userid
        String randomUserid = UUID.randomUUID().toString();
        user.setUserId(randomUserid);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList=userRepository.findAll();
        for(User user:userList){
            Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
            List<Rating> ratings = Arrays.stream(ratingsOfUser).collect(Collectors.toList());
            List<Rating> ratingList = ratings.stream().map(rating -> {
                //api call to hotel service to get the hotel
                //http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
                //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                // logger.info("response status code: {} ",forEntity.getStatusCode());
                //set the hotel to rating
                rating.setHotel(hotel);
                //return the rating
                return rating;
            }).collect(Collectors.toList());

            user.setRatings(ratingList);
        }
        return userList;
    }

    //get single user
    @Override
    public User getUser(String userId) {
        //get user from database with the help  of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));
        // fetch rating of the above  user from RATING SERVICE
        //http://localhost:8083/ratings/users/47e38dac-c7d0-4c40-8582-11d15f185fad

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        logger.info("{} ", ratingsOfUser);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).collect(Collectors.toList());
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            // logger.info("response status code: {} ",forEntity.getStatusCode());
            //set the hotel to rating
            rating.setHotel(hotel);
            //return the rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }
}
