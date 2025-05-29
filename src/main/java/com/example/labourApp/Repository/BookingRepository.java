package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings , Integer> {
}
