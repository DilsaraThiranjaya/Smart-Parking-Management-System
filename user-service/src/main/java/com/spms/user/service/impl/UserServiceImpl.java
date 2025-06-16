package com.spms.user.service.impl;

import com.spms.user.dto.UserRequest;
import com.spms.user.dto.LoginRequest;
import com.spms.user.dto.BookingRequest;
import com.spms.user.model.User;
import com.spms.user.model.User.UserRole;
import com.spms.user.model.User.UserStatus;
import com.spms.user.model.BookingHistory;
import com.spms.user.model.BookingHistory.BookingStatus;
import com.spms.user.repository.UserRepository;
import com.spms.user.repository.BookingHistoryRepository;
import com.spms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    @Override
    public User createUser(UserRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username " + request.getUsername() + " already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email " + request.getEmail() + " already exists");
        }
        
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getFirstName(),
            request.getLastName(),
            request.getPhoneNumber(),
            request.getRole()
        );
        
        return userRepository.save(user);
    }
    
    @Override
    public User updateUser(Long id, UserRequest request) {
        User existingUser = getUserById(id);
        
        // Check if new username conflicts with existing ones (excluding current user)
        userRepository.findByUsername(request.getUsername())
            .ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new RuntimeException("Username " + request.getUsername() + " already exists");
                }
            });
        
        // Check if new email conflicts with existing ones (excluding current user)
        userRepository.findByEmail(request.getEmail())
            .ifPresent(user -> {
                if (!user.getId().equals(id)) {
                    throw new RuntimeException("Email " + request.getEmail() + " already exists");
                }
            });
        
        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setRole(request.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    @Override
    public User authenticateUser(LoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User account is not active");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        return user;
    }
    
    @Override
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
    
    @Override
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }
    
    @Override
    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            searchTerm, searchTerm);
    }
    
    @Override
    public User updateUserStatus(Long id, UserStatus status) {
        User user = getUserById(id);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public BookingHistory createBooking(BookingRequest request) {
        // Verify user exists
        getUserById(request.getUserId());
        
        BookingHistory booking = new BookingHistory(
            request.getUserId(),
            request.getParkingSpaceId(),
            request.getVehicleId()
        );
        booking.setNotes(request.getNotes());
        
        return bookingHistoryRepository.save(booking);
    }
    
    @Override
    public List<BookingHistory> getUserBookingHistory(Long userId) {
        return bookingHistoryRepository.findByUserIdOrderByBookingTimeDesc(userId);
    }
    
    @Override
    public List<BookingHistory> getAllBookings() {
        return bookingHistoryRepository.findAll();
    }
    
    @Override
    public BookingHistory getBookingById(Long bookingId) {
        return bookingHistoryRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
    }

    @Override
    public BookingHistory updateBookingStatus(Long bookingId, BookingStatus status) {
        BookingHistory booking = getBookingById(bookingId);
        booking.setStatus(status);
        
        if (status == BookingStatus.COMPLETED) {
            booking.setEndTime(LocalDateTime.now());
        }
        
        return bookingHistoryRepository.save(booking);
    }
    
    @Override
    public List<BookingHistory> getActiveBookings() {
        return bookingHistoryRepository.findByStatus(BookingStatus.ACTIVE);
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<User> allUsers = userRepository.findAll();
        List<BookingHistory> allBookings = bookingHistoryRepository.findAll();
        
        long totalUsers = allUsers.size();
        long activeUsers = userRepository.findByStatus(UserStatus.ACTIVE).size();
        long drivers = userRepository.findByRole(UserRole.DRIVER).size();
        long parkingOwners = userRepository.findByRole(UserRole.PARKING_OWNER).size();
        long admins = userRepository.findByRole(UserRole.ADMIN).size();
        
        long totalBookings = allBookings.size();
        long activeBookings = bookingHistoryRepository.findByStatus(BookingStatus.ACTIVE).size();
        long completedBookings = bookingHistoryRepository.findByStatus(BookingStatus.COMPLETED).size();
        
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("inactiveUsers", totalUsers - activeUsers);
        stats.put("drivers", drivers);
        stats.put("parkingOwners", parkingOwners);
        stats.put("admins", admins);
        stats.put("totalBookings", totalBookings);
        stats.put("activeBookings", activeBookings);
        stats.put("completedBookings", completedBookings);
        
        return stats;
    }
}