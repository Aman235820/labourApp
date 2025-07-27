package com.example.labourApp.Controller;

import com.example.labourApp.Externals.CDNService;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/labour")
public class LabourController {

    @Autowired
    private LabourService labourService;

    @Autowired
    private CDNService cdnService;


    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "Working fine";
    }

    @PatchMapping("/updateLabourDetails")
    public Callable<ResponseEntity<ResponseDTO>> updateLabourDetails(@RequestBody LabourDTO labourDTO) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = labourService.updateLabourDetails(labourDTO);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Unable to update Labour !! Error : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @GetMapping("/showMyReviews/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> showMyReviews(
            @PathVariable Integer labourId,
            @RequestParam String sortBy,
            @RequestParam String sortOrder
    ) {
        return () -> {
            try {

                CompletableFuture<ResponseDTO> res = labourService.showMyReviews(labourId, sortBy, sortOrder);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Unable to fetch reviews !!"), HttpStatus.BAD_REQUEST);
            }

        };
    }

    @GetMapping("/showMyRatings/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> showMyRatings(@PathVariable Integer labourId) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = labourService.showMyRatings(labourId);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Unable to fetch ratings :" + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @GetMapping("/showRequestedServices/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> showRequestedServices(@PathVariable Integer labourId) {

        return () -> {
            try {

                CompletableFuture<ResponseDTO> response = labourService.showRequestedServices(labourId);

                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to fetch"), HttpStatus.BAD_REQUEST);
            }
        };

    }

    @GetMapping("/setBookingStatus")
    public Callable<ResponseEntity<ResponseDTO>> setBookingStatus(

            @RequestParam Integer labourId,
            @RequestParam Integer bookingId,
            @RequestParam Integer bookingStatusCode

    ) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> response = labourService.setBookingStatus(labourId, bookingId, bookingStatusCode);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to change status"), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @PatchMapping("/updateAdditionalLabourData")
    public Callable<ResponseEntity<ResponseDTO>> updateAdditionalLabourData(@RequestBody Map<String, Object> details) {

        return () -> {
            try {
                if (details.containsKey("labourId") && details.get("labourId") != null) {
                    CompletableFuture<ResponseDTO> res = labourService.updateAdditionalLabourData(details);

                    return new ResponseEntity<>(res.get(), HttpStatus.OK);
                } else {
                    throw new Exception("LabourId missing !!");
                }

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to store data : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };

    }


    @GetMapping("/getAdditionalLabourDetails/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> getAdditionalLabourDetails(@PathVariable Integer labourId) {
        return () -> {

            try {
                CompletableFuture<ResponseDTO> res = labourService.getAdditionalLabourDetails(labourId);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get data : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }

        };
    }


    @PostMapping("/uploadImage/{labourId}")
    public Callable<ResponseEntity<?>> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable Integer labourId) {
        return () -> {
            try {
                if (!file.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("Only image uploads are allowed.");
                }

                String imageUrl = cdnService.uploadImage(file, labourId);

                labourService.saveProfileImagetoDB(imageUrl, labourId);

                return ResponseEntity.ok(Map.of("url", imageUrl, "labourId", labourId));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Upload failed: " + e.getMessage());
            }
        };
    }

    @DeleteMapping("/deleteLabourImage/{labourId}")
    public Callable<ResponseEntity<?>> deleteLabourImage(@PathVariable Integer labourId) {
        return () -> {
            try {
                cdnService.deleteAllImagesByLabourId(labourId);
                labourService.removeProfileImage(labourId);
                return ResponseEntity.ok("All images deleted for labour ID: " + labourId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Deletion failed: " + e.getMessage());
            }
        };
    }


}
