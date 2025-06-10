package com.Hostel.Service;


import com.Hostel.Entity.AssetsSettings;

import java.util.List;

public interface AssetsSettingsService {
    AssetsSettings create(AssetsSettings settings);
    AssetsSettings update(Long id, AssetsSettings settings);
    void delete(Long id);
    AssetsSettings getById(Long id);
    List<AssetsSettings> getAll();
    List<AssetsSettings> getBySettingType(String settingType);
}
