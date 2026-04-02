package com.example.labourApp.Service.ServiceImpl;


import com.example.labourApp.Entity.mongo.Enterprise;
import com.example.labourApp.Entity.sql.EnterpriseLabourEntity;
import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.EnterpriseLabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.mongo.EnterpriseRepository;
import com.example.labourApp.Repository.sql.EnterpriseLabourRepository;
import com.example.labourApp.Service.EnterpriseService;
import com.example.labourApp.Service.MongoDocumentService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "spring.data.mongodb.uri", matchIfMissing = false)
public class EnterpriseServiceImpl implements EnterpriseService {

    private static String normalizeHeaderToken(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.trim().toLowerCase(Locale.ROOT).replaceAll("[\\s_-]+", "");
    }

    /**
     * Maps normalized Excel header text to EnterpriseLabourDTO property names.
     * Accepts common aliases (snake_case, spaces, etc.).
     */
    private static String resolveHeaderKey(String rawHeader) {
        String n = normalizeHeaderToken(rawHeader);
        if (n.isEmpty()) {
            return null;
        }
        return switch (n) {
            case "enterpriselabourid", "labourid" -> "enterpriseLabourId";
            case "enterpriseid", "enterprise" -> "enterpriseId";
            case "fullname", "name", "labourname", "employeename" -> "fullName";
            case "mobile", "mobilenumber", "phone", "contact", "primarymobile" -> "mobile";
            case "alternatemobile", "altmobile", "secondaryphone" -> "alternateMobile";
            case "email" -> "email";
            case "role", "designation" -> "role";
            case "primaryskill", "skill", "skills" -> "primarySkill";
            case "location", "worklocation", "site" -> "location";
            case "emergencycontactmobile", "emergencycontact", "emergencymobile" -> "emergencyContactMobile";
            case "profileimageurl", "profileimage", "photo" -> "profileImageUrl";
            case "iddocumenturl", "documenturl", "idproof" -> "idDocumentUrl";
            case "notes", "remarks" -> "notes";
            case "status" -> "status";
            case "verificationstatus" -> "verificationStatus";
            case "joinedat", "joiningdate" -> "joinedAt";
            case "admincomments" -> "adminComments";
            case "registrationtime", "registeredat" -> "registrationTime";
            default -> null;
        };
    }

    private static String stripTrailingDecimalZero(String v) {
        if (v == null || v.isEmpty()) {
            return v;
        }
        if (v.matches("^-?\\d+\\.0+$")) {
            return v.substring(0, v.indexOf('.'));
        }
        return v;
    }

    private static void applyEnterpriseLabourField(EnterpriseLabourDTO dto, String fieldKey, String rawValue) {
        if (fieldKey == null || rawValue == null) {
            return;
        }
        String value = stripTrailingDecimalZero(rawValue.trim());
        if (value.isEmpty()) {
            return;
        }
        switch (fieldKey) {
            case "enterpriseLabourId" -> {
                try {
                    dto.setEnterpriseLabourId((int) Double.parseDouble(value));
                } catch (NumberFormatException ignored) {
                    try {
                        dto.setEnterpriseLabourId(Integer.parseInt(value));
                    } catch (NumberFormatException ignored2) { /* skip invalid id */ }
                }
            }
            case "enterpriseId" -> dto.setEnterpriseId(value);
            case "fullName" -> dto.setFullName(value);
            case "mobile" -> dto.setMobile(value.replaceAll("\\s+", ""));
            case "alternateMobile" -> dto.setAlternateMobile(value.replaceAll("\\s+", ""));
            case "email" -> dto.setEmail(value);
            case "role" -> dto.setRole(value);
            case "primarySkill" -> dto.setPrimarySkill(value);
            case "location" -> dto.setLocation(value);
            case "emergencyContactMobile" -> dto.setEmergencyContactMobile(value.replaceAll("\\s+", ""));
            case "profileImageUrl" -> dto.setProfileImageUrl(value);
            case "idDocumentUrl" -> dto.setIdDocumentUrl(value);
            case "notes" -> dto.setNotes(value);
            case "status" -> dto.setStatus(value);
            case "verificationStatus" -> dto.setVerificationStatus(value);
            case "joinedAt" -> dto.setJoinedAt(value);
            case "adminComments" -> dto.setAdminComments(value);
            case "registrationTime" -> dto.setRegistrationTime(value);
            default -> { /* unknown mapped key */ }
        }
    }

    private static boolean rowHasRequiredFields(EnterpriseLabourDTO dto) {
        return dto.getEnterpriseId() != null && !dto.getEnterpriseId().isBlank()
                && dto.getFullName() != null && !dto.getFullName().isBlank()
                && dto.getMobile() != null && !dto.getMobile().isBlank();
    }

    private static String cellToString(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        String s = formatter.formatCellValue(cell);
        return s != null ? s.trim() : "";
    }

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    MongoDocumentService mongoDocumentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnterpriseLabourRepository enterpriseLabourRepository;

    @Async
    public CompletableFuture<ResponseDTO> registerEnterprise(EnterpriseDTO details) {

        String mobileNo = details.getOwnerContactInfo();

        boolean isExists = enterpriseRepository.existsByOwnerContactInfo(mobileNo);

        if (isExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Mobile Number already registered, try different !!"));
        }

        // Check if GST number already exists (if provided)
        if (details.getGstNumber() != null && !details.getGstNumber().trim().isEmpty()) {
            boolean gstExists = enterpriseRepository.existsByGstNumber(details.getGstNumber());
            if (gstExists) {
                return CompletableFuture.completedFuture(new ResponseDTO(null, true, "GST Number already registered, try different !!"));
            }
        }

        Enterprise enterprise = mapDtoToEntity(details);
        // Set default values for registration
        enterprise.setVerificationStatus("PENDING");
        enterprise.setRating("0.0");
        enterprise.setRatingCount("0");

        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        EnterpriseDTO responseDTO = mapEntityToDto(savedEnterprise);

        return CompletableFuture.completedFuture(new ResponseDTO(responseDTO, false, "Successfully Registered !!"));

    }

    @Async
    public CompletableFuture<ResponseDTO> enterpriseLogin(String mobileNumber) {
        Optional<Enterprise> e = enterpriseRepository.findByOwnerContactInfo(mobileNumber);
        if (e.isPresent()) {
            Enterprise enterprise = e.get();
            EnterpriseDTO enterpriseDTO = mapEntityToDto(enterprise);
            return CompletableFuture.completedFuture(new ResponseDTO(enterpriseDTO, false, "Successfully Fetched enterprise !!"));
        }

        return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Didn't find any enterprise with this mobile number !!"));
    }


    @Async
    public CompletableFuture<ResponseDTO> updateEnterpriseField(String id, Map<String, Object> updatedField) {
        try {
            // Call the generic mongo service to perform the update
            CompletableFuture<ResponseDTO> updateResult = mongoDocumentService.updateDocument("enterprise", id, updatedField);
            ResponseDTO result = updateResult.get();

            if (!result.getHasError()) {
                // Create response with only updated fields
                Map<String, Object> responseData = new HashMap<>(updatedField);
                responseData.put("id", id);
                responseData.put("updatedAt", System.currentTimeMillis());

                return CompletableFuture.completedFuture(new ResponseDTO(responseData, false, "Enterprise data updated successfully!"));
            } else {
                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Failed to update enterprise: " + e.getMessage()));
        }
    }

    @Async
    public CompletableFuture<ResponseDTO> findEnterpriseById(String enterpriseId) {
        return mongoDocumentService.findDocumentById("enterprise", enterpriseId);
    }


    @Async
    public CompletableFuture<ResponseDTO> findLabourByEnterpriseID(String enterpriseId) {

        List<EnterpriseLabourEntity> list = enterpriseLabourRepository.findEnterpriseLabourEntitiesByEnterpriseId(enterpriseId);

        return CompletableFuture.completedFuture(new ResponseDTO(list, false, "Successfully fetched all labours !!"));

    }


    @Async
    public CompletableFuture<ResponseDTO> enterpriseLabourOnboarding(EnterpriseLabourDTO enterpriseLabourDTO) {


        try {

            boolean validEnterprise = enterpriseRepository.existsById(enterpriseLabourDTO.getEnterpriseId());

            if (validEnterprise) {
                EnterpriseLabourEntity e = modelMapper.map(enterpriseLabourDTO, EnterpriseLabourEntity.class);

                enterpriseLabourRepository.save(e);

                EnterpriseLabourDTO edto = modelMapper.map(e, EnterpriseLabourDTO.class);

                return CompletableFuture.completedFuture(new ResponseDTO(edto, false, "Labour onboarded successfully !!"));

            }

            throw new Exception("Enterprise not registered !!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Async
    public CompletableFuture<ResponseDTO> bulkUploadEnterpriseLabour(InputStream inputStream) {
        try {
            List<EnterpriseLabourDTO> dtos = readExcelFile(inputStream);
            if (dtos.isEmpty()) {
                return CompletableFuture.completedFuture(new ResponseDTO(null, true, "No valid rows found in spreadsheet (need enterpriseId, fullName, mobile)."));
            }
            Set<String> enterpriseIds = dtos.stream()
                    .map(EnterpriseLabourDTO::getEnterpriseId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            for (String eid : enterpriseIds) {
                if (!enterpriseRepository.existsById(eid)) {
                    return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Unknown enterprise id: " + eid));
                }
            }
            List<EnterpriseLabourEntity> entities = dtos.stream()
                    .map(dto -> {
                        EnterpriseLabourEntity e = modelMapper.map(dto, EnterpriseLabourEntity.class);
                        e.setEnterpriseLabourId(null);
                        return e;
                    })
                    .collect(Collectors.toList());
            enterpriseLabourRepository.saveAll(entities);
            return CompletableFuture.completedFuture(new ResponseDTO(dtos.size(), false, "Bulk upload saved " + entities.size() + " labour record(s)."));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Failed to read spreadsheet: " + e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<EnterpriseLabourDTO> readExcelFile(InputStream is) throws IOException {
        List<EnterpriseLabourDTO> list = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) {
                return list;
            }
            Row headerRow = rows.next();
            List<String> columnFieldKeys = new ArrayList<>();
            for (Cell cell : headerRow) {
                columnFieldKeys.add(resolveHeaderKey(cellToString(cell, formatter)));
            }
            int width = columnFieldKeys.size();
            while (rows.hasNext()) {
                Row row = rows.next();
                if (row == null) {
                    continue;
                }
                EnterpriseLabourDTO dto = new EnterpriseLabourDTO();
                for (int i = 0; i < width; i++) {
                    String fieldKey = columnFieldKeys.get(i);
                    if (fieldKey == null) {
                        continue;
                    }
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    applyEnterpriseLabourField(dto, fieldKey, cellToString(cell, formatter));
                }
                if (rowHasRequiredFields(dto)) {
                    list.add(dto);
                }
            }
        }
        return list;
    }


    private Enterprise mapDtoToEntity(EnterpriseDTO dto) {
        Enterprise enterprise = new Enterprise();
        enterprise.setOwnername(dto.getOwnername());
        enterprise.setOwnerContactInfo(dto.getOwnerContactInfo());
        enterprise.setCompanyName(dto.getCompanyName());
        enterprise.setGstNumber(dto.getGstNumber());
        enterprise.setOtherContactNumbers(dto.getOtherContactNumbers());
        enterprise.setRegistrationCertificateLink(dto.getRegistrationCertificateLink());
        enterprise.setWorkforceSize(dto.getWorkforceSize());
        enterprise.setServicesOffered(dto.getServicesOffered());
        enterprise.setOtherDocumentLinks(dto.getOtherDocumentLinks());
        enterprise.setRating(dto.getRating());
        enterprise.setRatingCount(dto.getRatingCount());
        enterprise.setLocation(dto.getLocation());
        enterprise.setVerificationStatus(dto.getVerificationStatus());
        enterprise.setVerifiedAt(dto.getVerifiedAt());
        enterprise.setAdminComments(dto.getAdminComments());
        enterprise.setRegistrationTime(dto.getRegistrationTime());
        return enterprise;
    }

    private EnterpriseDTO mapEntityToDto(Enterprise enterprise) {
        EnterpriseDTO dto = new EnterpriseDTO();
        dto.setId(enterprise.getId());
        dto.setOwnername(enterprise.getOwnername());
        dto.setOwnerContactInfo(enterprise.getOwnerContactInfo());
        dto.setCompanyName(enterprise.getCompanyName());
        dto.setGstNumber(enterprise.getGstNumber());
        dto.setOtherContactNumbers(enterprise.getOtherContactNumbers());
        dto.setRegistrationCertificateLink(enterprise.getRegistrationCertificateLink());
        dto.setWorkforceSize(enterprise.getWorkforceSize());
        dto.setServicesOffered(enterprise.getServicesOffered());
        dto.setOtherDocumentLinks(enterprise.getOtherDocumentLinks());
        dto.setRating(enterprise.getRating());
        dto.setRatingCount(enterprise.getRatingCount());
        dto.setLocation(enterprise.getLocation());
        dto.setVerificationStatus(enterprise.getVerificationStatus());
        dto.setVerifiedAt(enterprise.getVerifiedAt());
        dto.setAdminComments(enterprise.getAdminComments());
        dto.setRegistrationTime(enterprise.getRegistrationTime());

        return dto;
    }

}
