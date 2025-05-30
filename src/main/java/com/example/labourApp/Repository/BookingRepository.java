package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings , Integer> {

    Optional<Bookings> findByBookingIdAndUserIdAndLabourId(Integer bookingId, Integer userId, Integer labourId);

    Optional<List<Bookings>> findByUserId(Integer userId);
}
