package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.Review;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Service.LabourService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Data
@Service
public class LabourServiceImpl implements LabourService {

    @Autowired
    private LabourRepository labourRepository;

    @Async
    public CompletableFuture<ResponseDTO> registerLabour(LabourDTO details) {
        Labour labour = mapDtoToEntity(details);
        labourRepository.save(labour);
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Registered !!"));
    }

    @Async
    public CompletableFuture<ResponseDTO> findLabourByCategory(String category) {
        List<Labour> labourList = labourRepository.findByLabourSkill(category);
        List<LabourDTO> dtoList = labourList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new ResponseDTO(dtoList, false, "Fetched successfully"));
    }

    @Async
    public CompletableFuture<ResponseDTO> findAllLabours() {
        List<Labour> labourList = labourRepository.findAll();
        List<LabourDTO> dtoList = labourList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new ResponseDTO(dtoList, false, "Fetched successfully"));
    }

    @Async
    public CompletableFuture<ResponseDTO> findLabour(Integer labourId) {
        Optional<Labour> labour = labourRepository.findById(labourId);
        if(labour.isPresent()) {
            LabourDTO dto = mapEntityToDto(labour.get());
            return CompletableFuture.completedFuture(new ResponseDTO(dto, false, "Fetched successfully"));
        }
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Unable to fetch !!"));
    }

    private Labour mapDtoToEntity(LabourDTO dto) {
        Labour labour = new Labour();
        labour.setLabourName(dto.getLabourName());
        labour.setLabourSkill(dto.getLabourSkill());
        labour.setLabourMobileNo(dto.getLabourMobileNo());
        labour.setRating(dto.getRating());
        labour.setRatingCount(dto.getRatingCount());
        labour.setReviews(dto.getReviews());
        return labour;
    }

    private LabourDTO mapEntityToDto(Labour labour) {
        LabourDTO dto = new LabourDTO();
        dto.setLabourId(labour.getLabourId());
        dto.setLabourName(labour.getLabourName());
        dto.setLabourSkill(labour.getLabourSkill());
        dto.setLabourMobileNo(labour.getLabourMobileNo());
        dto.setRating(labour.getRating());
        dto.setRatingCount(labour.getRatingCount());
        dto.setReviews(labour.getReviews());

        return dto;

    }
}

