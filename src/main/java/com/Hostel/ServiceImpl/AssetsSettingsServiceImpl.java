package com.Hostel.ServiceImpl;


import com.Hostel.Entity.AssetsSettings;
import com.Hostel.Repository.AssetsSettingsRepository;
import com.Hostel.Service.AssetsSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetsSettingsServiceImpl implements AssetsSettingsService {

    @Autowired
    private AssetsSettingsRepository repository;

    @Override
    public AssetsSettings create(AssetsSettings settings) {
        return repository.save(settings);
    }

    @Override
    public AssetsSettings update(Long id, AssetsSettings settings) {
        AssetsSettings existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Setting not found"));
        existing.setSettingType(settings.getSettingType());
        existing.setSettingValue(settings.getSettingValue());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public AssetsSettings getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Setting not found"));
    }

    @Override
    public List<AssetsSettings> getAll() {
        return repository.findAll();
    }

    @Override
    public List<AssetsSettings> getBySettingType(String settingType) {
        return repository.findBySettingType(settingType);
    }
}
