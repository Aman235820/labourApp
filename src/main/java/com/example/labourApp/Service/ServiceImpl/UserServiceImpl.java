package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Bookings;
import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.Review;
import com.example.labourApp.Entity.User;
import com.example.labourApp.Models.BookingDTO;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Repository.BookingRepository;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    LabourRepository labourRepository;

    @Async
    public CompletableFuture<ResponseDTO> createUser(UserDTO request) {


        String email = request.getEmail();

        boolean isAlreadyExists = userRepository.existsByEmail(email);

        if (isAlreadyExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Already a registered emailID , try different !!"));
        }

        User user = mapper.convertValue(request, User.class);

        userRepository.save(user);

        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Registered Successfully"));

    }


    @Async
    public CompletableFuture<ResponseDTO> loginUser(UserDTO request) {

        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "User not found !!"));
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Wrong password !!"));
        }

        Map<String, String> result = new HashMap<>();
        result.put("name", user.getFullName());
        result.put("email", user.getEmail());

        return CompletableFuture.completedFuture(new ResponseDTO(result, false, "success"));
    }


    @Async
    public CompletableFuture<ResponseDTO> bookLabourService(BookingDTO bookingDetails) {
        int userId = bookingDetails.getUserId();
        int labourId = bookingDetails.getLabourId();

        if (userRepository.existsById(userId) && labourRepository.existsById(labourId)) {
            Bookings book = mapBookingDTOToEntity(bookingDetails);
            Bookings bookedData = bookingRepository.save(book);
            
            HashMap<String, Object> resMap = new HashMap<>();
            resMap.put("bookingId", bookedData.getBookingId());
            resMap.put("bookingTime", bookedData.getBookingTime());
            
            return CompletableFuture.completedFuture(new ResponseDTO(resMap, false, "Booking Successful !!"));
        } else {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Booking not possible !!"));
        }
    }

    /**
     * Maps BookingDTO to Bookings entity
     *
     * @param bookingDTO The DTO to convert
     * @return Bookings entity
     */
    private Bookings mapBookingDTOToEntity(BookingDTO bookingDTO) {
        Bookings booking = new Bookings();
        booking.setUserId(bookingDTO.getUserId());
        booking.setLabourId(bookingDTO.getLabourId());
        booking.setLabourMobileNo(bookingDTO.getLabourMobileNo());
        booking.setLabourSkill(bookingDTO.getLabourSkill());
        booking.setUserMobileNumber(bookingDTO.getUserMobileNumber());
        
        // Format current time as string
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        booking.setBookingTime(formattedDateTime);
        
        return booking;
    }

    /**
     * Maps Bookings entity to BookingDTO
     *
     * @param booking The entity to convert
     * @return BookingDTO
     */
    private BookingDTO mapEntityToBookingDTO(Bookings booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(booking.getUserId());
        bookingDTO.setLabourId(booking.getLabourId());
        bookingDTO.setLabourMobileNo(booking.getLabourMobileNo());
        bookingDTO.setLabourSkill(booking.getLabourSkill());
        bookingDTO.setUserMobileNumber(booking.getUserMobileNumber());
        bookingDTO.setBookingTime(booking.getBookingTime());
        return bookingDTO;
    }

}
