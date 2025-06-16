package com.spms.user.repository;

import com.spms.user.model.BookingHistory;
import com.spms.user.model.BookingHistory.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long> {
    
    List<BookingHistory> findByUserId(Long userId);
    
    List<BookingHistory> findByUserIdOrderByBookingTimeDesc(Long userId);
    
    List<BookingHistory> findByParkingSpaceId(Long parkingSpaceId);
    
    List<BookingHistory> findByStatus(BookingStatus status);
    
    List<BookingHistory> findByUserIdAndStatus(Long userId, BookingStatus status);
    
    List<BookingHistory> findByVehicleId(Long vehicleId);
}