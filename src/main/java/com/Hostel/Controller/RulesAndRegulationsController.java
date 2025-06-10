package com.Hostel.Controller;


import com.Hostel.Entity.RulesAndRegulations;
import com.Hostel.Service.RulesAndRegulationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class RulesAndRegulationsController {

    @Autowired
    private RulesAndRegulationsService rulesAndRegulationsService;

    // ✅ Add a rule
    @PostMapping("/addRule")
    public ResponseEntity<RulesAndRegulations> addRule(@RequestBody RulesAndRegulations rule) {
        RulesAndRegulations newRule = rulesAndRegulationsService.addRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRule);
    }

    // ✅ Get all rules
    @GetMapping("/getRuleById/getAllRules")
    public ResponseEntity<List<RulesAndRegulations>> getAllRules() {
        return ResponseEntity.ok(rulesAndRegulationsService.getAllRules());
    }

    // ✅ Get rule by ID
    @GetMapping("/getRuleById/{ruleId}")
    public ResponseEntity<RulesAndRegulations> getRuleById(@PathVariable Long ruleId) {
        return ResponseEntity.ok(rulesAndRegulationsService.getRuleById(ruleId));
    }

    // ✅ Update a rule
    @PutMapping("/updateRule/{ruleId}")
    public ResponseEntity<RulesAndRegulations> updateRule(
            @PathVariable Long ruleId,
            @RequestBody RulesAndRegulations rule) {
        RulesAndRegulations updatedRule = rulesAndRegulationsService.updateRule(ruleId, rule);
        return ResponseEntity.ok(updatedRule);
    }

    // ✅ Delete a rule
    @DeleteMapping("/deleteRule/{ruleId}")
    public ResponseEntity<String> deleteRule(@PathVariable Long ruleId) {
        rulesAndRegulationsService.deleteRule(ruleId);
        return ResponseEntity.ok("Rule deleted successfully!");
    }
}
