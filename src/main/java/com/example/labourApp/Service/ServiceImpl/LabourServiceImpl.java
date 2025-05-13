package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Service.LabourService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
@Service
public class LabourServiceImpl implements LabourService {

    @Autowired
    private LabourRepository labourRepository;

    private Labour mapDtoToEntity(LabourDTO dto) {
        Labour labour = new Labour();
        labour.setLabourName(dto.getLabourName());
        labour.setLabourSkill(dto.getLabourSkill());
        labour.setLabourMobileNo(dto.getLabourMobileNo());
        return labour;
    }

    @Async
    public CompletableFuture<ResponseDTO> registerLabour(LabourDTO details) {

        // Use manual mapping instead of ModelMapper
        Labour labour = mapDtoToEntity(details);

        labourRepository.save(labour); // The actual DB query is done in another thread (because of @Async).

        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Registered !!"));

    }

    public CompletableFuture<ResponseDTO> findLabourByCategory(String category) {

        List<Labour> labourList = labourRepository.findByLabourSkill(category);

        return CompletableFuture.completedFuture(new ResponseDTO(labourList, false, "Fetched successfully"));

    }

    public CompletableFuture<ResponseDTO> findAllLabours() {
        List<Labour> labourList = labourRepository.findAll();
        return CompletableFuture.completedFuture(new ResponseDTO(labourList, false, "Fetched successfully"));

    }


}

