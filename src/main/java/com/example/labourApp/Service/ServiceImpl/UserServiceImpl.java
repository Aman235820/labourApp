package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.Review;
import com.example.labourApp.Entity.User;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    LabourRepository labourRepository;

    @Async
    public CompletableFuture<ResponseDTO> createUser(UserDTO request) {

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

        return CompletableFuture.completedFuture(new ResponseDTO(result, false, "Successfully logged In !!"));
    }



}
