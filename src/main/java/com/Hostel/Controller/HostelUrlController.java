package com.Hostel.Controller;

import com.Hostel.Entity.HostelUrlMapping;
import com.Hostel.Exception.HostelUrlMappingException;
import com.Hostel.Service.HostelUrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
@Slf4j
public class HostelUrlController {


    @Autowired
    private  HostelUrlMappingService  hostelUrlMappingService;

    @Value("${hostel.website.base-url}")
    private String baseUrl;


    @PostMapping("/addHostelDynamicUrl")
    public ResponseEntity<?> addHostelDynamicUrl(@RequestBody Map<String, String> request) {
        String dynamicPart = request.get("dynamicPart");

        log.info("Received request to add hostel dynamic URL: {}", dynamicPart);

        try {
            HostelUrlMapping mapping = hostelUrlMappingService.createHostelUrlMapping(dynamicPart.trim());
            String fullUrl = baseUrl + "/" + mapping.getDynamicPart();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", mapping.getId());
            responseData.put("dynamicPart", mapping.getDynamicPart());
            responseData.put("fullUrl", fullUrl);  // 🟢 Full URL included

            return ResponseEntity.ok(createSuccessResponse("Hostel URL mapping created successfully", responseData));
        } catch (HostelUrlMappingException e) {
            log.error("Failed to create Hostel URL mapping: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/getHostelUrlMapping/{dynamicPart}")
    public ResponseEntity<?> getHostelUrlMapping(@PathVariable String dynamicPart) {
        log.info("Fetching hostel URL mapping for dynamic part: {}", dynamicPart);

        try {
            HostelUrlMapping mapping = hostelUrlMappingService.getHostelUrlMapping(dynamicPart.trim());
            return ResponseEntity.ok(createSuccessResponse("Hostel URL mapping retrieved successfully", mapping));
        } catch (HostelUrlMappingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/getAllHostelUrlMappings")
    public ResponseEntity<?> getAllHostelUrlMappings() {
        try {
            List<HostelUrlMapping> mappings = hostelUrlMappingService.getAllHostelUrlMappings();
            return ResponseEntity.ok(createSuccessResponse("All Hostel URL mappings retrieved successfully", mappings));
        } catch (HostelUrlMappingException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/updateHostelUrlMapping/{id}")
    public ResponseEntity<?> updateHostelUrlMapping(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String dynamicPart = request.get("dynamicPart");
        log.info("Updating hostel URL mapping with ID: {} to dynamic part: {}", id, dynamicPart);

        try {
            HostelUrlMapping updatedMapping = hostelUrlMappingService.updateHostelUrlMapping(id, dynamicPart.trim());
            return ResponseEntity.ok(createSuccessResponse("Hostel URL mapping updated successfully", updatedMapping));
        } catch (HostelUrlMappingException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteHostelUrlMapping/{id}")
    public ResponseEntity<?> deleteHostelUrlMapping(@PathVariable Long id) {
        try {
            hostelUrlMappingService.deleteHostelUrlMapping(id);
            return ResponseEntity.ok(createSuccessResponse("Hostel URL mapping deleted successfully", null));
        } catch (HostelUrlMappingException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}
