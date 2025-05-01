package Controller;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/Labour")
public class LabourController {


    @Autowired
    private LabourService labourService;


    @PostMapping("registerLabour")
    public ResponseEntity<ResponseDTO> registerLabour(
            @RequestBody LabourDTO details
    ) {
        try {

            ResponseDTO response = labourService.registerLabour(details);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ce) {
            return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to register"), HttpStatus.BAD_REQUEST);
        }


    }


}
