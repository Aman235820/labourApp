package com.example.labourApp.Service.ServiceImpl;


import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.LabourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabourServiceImpl {

      @Autowired
      private ModelMapper modelMapper;

      @Autowired
      private LabourRepository labourRepository;

      public ResponseDTO registerLabour(LabourDTO details){


            Labour labour = modelMapper.map(details , Labour.class);

            labourRepository.save(labour);

            return new ResponseDTO(null,false, "Successfully Registered !!");


      }

}
