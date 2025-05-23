package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.User;
import com.example.labourApp.Models.*;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    LabourRepository labourRepository;

    @Autowired
    UserRepository userRepository;

    @Async
    public CompletableFuture<ResponseDTO> removeLabour(Integer labourId) {

        boolean isAlreadyExists = labourRepository.existsById(labourId);

        if(!isAlreadyExists){
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Labour not found in Database !!"));
        }

        labourRepository.deleteById(labourId);
             return CompletableFuture.completedFuture(new ResponseDTO(null,false,"Successfully Deleted"));
    }

    @Async
    public CompletableFuture<ResponseDTO> removeUser(Integer userId){

        boolean isAlreadyExists = userRepository.existsById(userId);

        if(!isAlreadyExists){
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "User not found in Database !!"));
        }

        userRepository.deleteById(userId);
        return CompletableFuture.completedFuture(new ResponseDTO(null,false,"Successfully Deleted"));
    }


    @Async
    public CompletableFuture<PaginationResponseDTO> findAllUsers(PaginationRequestDTO paginationRequestDTO) {

        Integer pageNumber = paginationRequestDTO.getPageNumber();
        Integer pageSize = paginationRequestDTO.getPageSize();
        String sortBy = paginationRequestDTO.getSortBy();
        String sortOrder = paginationRequestDTO.getSortOrder();

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> userPage = this.userRepository.findAll(p);

        List<User> userList = userPage.getContent();

        List<UserDTO> dtoList = userList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new PaginationResponseDTO(dtoList, userPage.getNumber(), userPage.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast()));
    }


    private UserDTO mapEntityToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }




}
