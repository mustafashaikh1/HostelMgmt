package com.Hostel.Controller;

import com.Hostel.Entity.HostelAssets;
import com.Hostel.Service.HostelAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class HostelAssetsController {

    @Autowired
    private HostelAssetsService assetsService;

    @PostMapping("/createAssets")
    public ResponseEntity<?> create(@RequestBody HostelAssets asset) {
        try {
            HostelAssets createdAsset = assetsService.createAsset(asset);
            return ResponseEntity.ok(createdAsset);
        } catch (DataIntegrityViolationException e) {
            String message = "Model number already exists.";
            if (e.getRootCause() != null && e.getRootCause().getMessage().contains("Duplicate entry")) {
                String errorMsg = e.getRootCause().getMessage();
                int start = errorMsg.indexOf("Duplicate entry '") + 17;
                int end = errorMsg.indexOf("'", start);
                String duplicateValue = errorMsg.substring(start, end);
                message = "Model number already exists: " + duplicateValue;
            }
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }
    }


    @PutMapping("/updateAssets/{id}")
    public HostelAssets update(@PathVariable Long id, @RequestBody HostelAssets asset) {
        return assetsService.updateAsset(id, asset);
    }

    @DeleteMapping("/deleteAssets/{id}")
    public void delete(@PathVariable Long id) {
        assetsService.deleteAsset(id);
    }

    @GetMapping("/getAssetsById/{id}")
    public HostelAssets getById(@PathVariable Long id) {
        return assetsService.getAssetById(id);
    }

    @GetMapping("/getAllAssets")
    public List<HostelAssets> getAll() {
        return assetsService.getAllAssets();
    }

}
