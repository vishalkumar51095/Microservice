package com.vishalkumar.user.service.controllers;

import com.vishalkumar.user.service.entities.User;
import com.vishalkumar.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Create a user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    // Get a single user with circuit breaker
    int retryCount =1 ;

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
    //@RateLimiter(name="userRateLimiter",fallbackMethod="ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("Get single user handler: UserController");

        logger.info("Retry count:{}",retryCount);
        retryCount++;
        User user = userService.getUser(userId);  // This must make REST calls to external services
        return ResponseEntity.ok(user);
    }

    // Fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Throwable ex) {
        logger.warn("Fallback executed for userId: {}, because: {}", userId, ex.toString());
        User user = User.builder()
                .userId("141234")
                .name("Dummy Vishal")
                .email("vishal@fallback.com")
                .about("This user is returned as fallback because some service is down")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUsers = userService.getAllUser();
        return ResponseEntity.ok(allUsers);
    }
}
