package com.spms.user.controller;

import com.spms.user.dto.UserRequest;
import com.spms.user.dto.LoginRequest;
import com.spms.user.dto.BookingRequest;
import com.spms.user.model.User;
import com.spms.user.model.User.UserRole;
import com.spms.user.model.User.UserStatus;
import com.spms.user.model.BookingHistory;
import com.spms.user.model.BookingHistory.BookingStatus;
import com.spms.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
        try {
            User createdUser = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                         @Valid @RequestBody UserRequest request) {
        try {
            User updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticateUser(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticateUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable("role") UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable("status") UserStatus status) {
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String term) {
        List<User> users = userService.searchUsers(term);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable("id") Long id,
                                               @RequestParam UserStatus status) {
        try {
            User updatedUser = userService.updateUserStatus(id, status);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/bookings")
    public ResponseEntity<BookingHistory> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingHistory booking = userService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<BookingHistory>> getUserBookingHistory(@PathVariable("userId") Long userId) {
        List<BookingHistory> bookings = userService.getUserBookingHistory(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingHistory>> getAllBookings() {
        List<BookingHistory> bookings = userService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingHistory> getBookingById(@PathVariable("bookingId") Long bookingId) {
        try {
            BookingHistory booking = userService.getBookingById(bookingId);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/bookings/{bookingId}/status")
    public ResponseEntity<BookingHistory> updateBookingStatus(@PathVariable("bookingId") Long bookingId,
                                                            @RequestParam BookingStatus status) {
        try {
            BookingHistory booking = userService.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/bookings/active")
    public ResponseEntity<List<BookingHistory>> getActiveBookings() {
        List<BookingHistory> bookings = userService.getActiveBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = userService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }
}