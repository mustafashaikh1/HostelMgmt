package com.Hostel.Controller;

import com.Hostel.Entity.DepositeDetails;
import com.Hostel.Repository.DepositeDetailsRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.DepositeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class DepositeDetailsController {

    @Autowired
    private DepositeDetailsService depositeDetailsService;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Autowired
    private DepositeDetailsRepository depositeDetailsRepository;

    @PostMapping("/createDepositDetails/{formNumber}")
    public ResponseEntity<DepositeDetails> createDepositDetails(@PathVariable String formNumber,
                                                                @RequestBody DepositeDetails depositeDetails) {
        DepositeDetails saved = depositeDetailsService.saveDepositeDetails(formNumber, depositeDetails);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/updateDepositDetails/{id}")
    public ResponseEntity<DepositeDetails> updateDepositDetails(@PathVariable Long id,
                                                                @RequestBody DepositeDetails updatedDetails) {
        return ResponseEntity.ok(depositeDetailsService.updateDepositDetails(id, updatedDetails));
    }


    @GetMapping("/getAllDeposite")
    public ResponseEntity<List<DepositeDetails>> getAll() {
        return ResponseEntity.ok(depositeDetailsService.getAllDepositeDetails());
    }

    @GetMapping("/getDepositeById/{id}")
    public ResponseEntity<DepositeDetails> getById(@PathVariable Long id) {
        return ResponseEntity.ok(depositeDetailsService.getDepositeDetailsById(id));
    }

    @DeleteMapping("/deleteDepositeById/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        depositeDetailsService.deleteDepositeDetails(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getDepositeSummary")
    public ResponseEntity<List<Map<String, Object>>> getRevenueSummary() {
        return ResponseEntity.ok(depositeDetailsService.getRevenueSummary());
    }

    @GetMapping("/getDepositSummaryByMonthYear")
    public ResponseEntity<List<Map<String, Object>>> getDepositSummaryByMonthYear(
            @RequestParam String monthName,
            @RequestParam String year) {

        List<Object[]> results = depositeDetailsService.getDepositSummaryByDate(monthName, year);

        List<Map<String, Object>> response = results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("createdDate", row[0]);
            map.put("count", row[1]);
            map.put("totalAmount", row[2]);
            map.put("monthName", monthName);
            return map;
        }).toList();

        return ResponseEntity.ok(response); // will return empty list if no results
    }


    // GET /api/deposits/summary/monthwise?year=2024
    @GetMapping("/summary/monthwise")
    public List<Map<String, Object>> getMonthWiseSummary(@RequestParam String year) {
        return depositeDetailsService.getMonthWiseSummary(year);
    }

}
