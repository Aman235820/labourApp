package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings , Integer> {

    Optional<Bookings> findByBookingIdAndUserIdAndLabourId(Integer bookingId, Integer userId, Integer labourId);

    Optional<List<Bookings>> findByUserId(Integer userId);

    Optional<List<Bookings>>  findByLabourId(Integer labourId);

    //@Query(value = "SELECT booking_status_code, COUNT(*) FROM defaultdb.bookings GROUP BY booking_status_code" , nativeQuery = true)
    @Query(value = """
            SELECT
                SUM(CASE WHEN booking_status_code = -1 THEN 1 ELSE 0 END) AS Rejected,
                SUM(CASE WHEN booking_status_code = 1 THEN 1 ELSE 0 END) AS Pending,
                SUM(CASE WHEN booking_status_code = 2 THEN 1 ELSE 0 END) AS Accepted,
                SUM(CASE WHEN booking_status_code = 3 THEN 1 ELSE 0 END) AS Completed
            FROM defaultdb.bookings;""" , nativeQuery = true)
    Object getBookingStatusStats();
}
