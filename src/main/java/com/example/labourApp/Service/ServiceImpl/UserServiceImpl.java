package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.sql.Bookings;
import com.example.labourApp.Entity.sql.Labour;
import com.example.labourApp.Entity.sql.Review;
import com.example.labourApp.Entity.sql.User;
import com.example.labourApp.Models.BookingDTO;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Repository.sql.BookingRepository;
import com.example.labourApp.Repository.sql.LabourRepository;
import com.example.labourApp.Repository.sql.UserRepository;
import com.example.labourApp.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserServiceImpl implements UserService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

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


        String mobile = request.getMobileNumber();

        boolean isAlreadyExists = userRepository.existsByMobileNumber(mobile);

        if (isAlreadyExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Already a registered mobile number , try different !!"));
        }

        User user = mapper.convertValue(request, User.class);

        userRepository.save(user);

        return CompletableFuture.completedFuture(new ResponseDTO(user, false, "Registered Successfully"));

    }


    @Async
    public CompletableFuture<ResponseDTO> loginUser(UserDTO request) {

        String email = request.getEmail();
        String password = request.getPassword();

        //Optional<User> userOptional = userRepository.findByEmail(email);

        Optional<User> userOptional = userRepository.findByMobileNumber(request.getMobileNumber());

        if (userOptional.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "User not found !!"));
        }

        User user = userOptional.get();
//        if (!user.getPassword().equals(password)) {
//            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Wrong password !!"));
//        }

        Map<String, Object> result = new HashMap<>();
        result.put("name", user.getFullName());
        result.put("email", user.getEmail());
        result.put("userId", user.getUserId());
        result.put("userMobileNumber", user.getMobileNumber());

        return CompletableFuture.completedFuture(new ResponseDTO(result, false, "success"));
    }


    @Async
    public CompletableFuture<ResponseDTO> bookLabourService(BookingDTO bookingDetails) {
        int userId = bookingDetails.getUserId();
        int labourId = bookingDetails.getLabourId();
        String labourServiceUsed = bookingDetails.getLabourSkill();

        return CompletableFuture.supplyAsync(() -> {
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Labour> labourOptional = labourRepository.findById(labourId);

            if (userOptional.isEmpty() || labourOptional.isEmpty()) {
                return new ResponseDTO(null, true, "User or Labour not found !!");
            }

            User user = userOptional.get();
            Labour labour = labourOptional.get();

            // Create booking with details from User and Labour
            Bookings book = new Bookings();
            book.setUserId(userId);
            book.setLabourId(labourId);
            book.setUserName(user.getFullName());
            book.setLabourName(labour.getLabourName());
            book.setLabourSkill(labourServiceUsed);
            book.setUserMobileNumber(user.getMobileNumber());
            book.setLabourMobileNo(labour.getLabourMobileNo());
            book.setBookingTime(bookingDetails.getBookingTime());

//            booking status code :
//            1 : Confirmation pending at labour's end
//            2 : Booking accepted
//            3 : Work done
//            -1 : Booking rejected by labour


            book.setBookingStatusCode(1);


            Bookings bookedData = bookingRepository.save(book);
            
            HashMap<String, Object> resMap = new HashMap<>();
            resMap.put("bookingId", bookedData.getBookingId());
            resMap.put("bookingTime", bookedData.getBookingTime());
            resMap.put("userName", bookedData.getUserName());
            resMap.put("labourName", bookedData.getLabourName());
            resMap.put("userMobileNumber", bookedData.getUserMobileNumber());
            resMap.put("labourMobileNo", bookedData.getLabourMobileNo());
            resMap.put("labourSkill", bookedData.getLabourSkill());
            resMap.put("bookingStatusCode" , bookedData.getBookingStatusCode());
            
            return new ResponseDTO(resMap, false, "Booking Successful !!");
        }, executorService);
    }


    @Async
    public CompletableFuture<ResponseDTO> viewMyBookings(Integer userId) {

        Optional<List<Bookings>> booking = bookingRepository.findByUserId(userId);
        return booking.map(bookings -> {
            if (bookings.isEmpty()) {
                return CompletableFuture.completedFuture(new ResponseDTO(null, false, "No Bookings Found !!"));
            }
            return CompletableFuture.completedFuture(new ResponseDTO(bookings, false, "Bookings Fetched Successfully !!"));
        }).orElseGet(() -> CompletableFuture.completedFuture(new ResponseDTO(null, false, "No Bookings Found !!")));

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
        booking.setBookingTime(bookingDTO.getBookingTime());

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
