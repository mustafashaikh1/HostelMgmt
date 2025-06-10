package com.Hostel.Controller;


import com.Hostel.Entity.AssetsSettings;
import com.Hostel.Service.AssetsSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class AssetsSettingsController {

    @Autowired
    private AssetsSettingsService service;

    @PostMapping("createAssetsSettings")
    public AssetsSettings create(@RequestBody AssetsSettings settings) {
        return service.create(settings);
    }

    @PutMapping("updateAssetsSettings/{id}")
    public AssetsSettings update(@PathVariable Long id, @RequestBody AssetsSettings settings) {
        return service.update(id, settings);
    }

    @DeleteMapping("deleteAssetsSettings/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("getAssetsSettingsById/{id}")
    public AssetsSettings getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("getAllAssetsSettings")
    public List<AssetsSettings> getAll() {
        return service.getAll();
    }

    @GetMapping("/getBySettingType/{settingType}")
    public List<AssetsSettings> getBySettingType(@PathVariable String settingType) {
        return service.getBySettingType(settingType);
    }
}
