package com.spms.user.service;

import com.spms.user.dto.BookingRequest;
import com.spms.user.dto.LoginRequest;
import com.spms.user.dto.UserRequest;
import com.spms.user.model.BookingHistory;
import com.spms.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User createUser(UserRequest request);

    User updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    User authenticateUser(LoginRequest request);

    List<User> getUsersByRole(User.UserRole role);

    List<User> getUsersByStatus(User.UserStatus status);

    List<User> searchUsers(String searchTerm);

    User updateUserStatus(Long id, User.UserStatus status);

    BookingHistory createBooking(BookingRequest request);

    List<BookingHistory> getUserBookingHistory(Long userId);

    List<BookingHistory> getAllBookings();

    BookingHistory getBookingById(Long bookingId);

    BookingHistory updateBookingStatus(Long bookingId, BookingHistory.BookingStatus status);

    List<BookingHistory> getActiveBookings();

    Map<String, Object> getUserStatistics();
}
