package com.Hostel.Repository;


import com.Hostel.Entity.AssetsSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetsSettingsRepository extends JpaRepository<AssetsSettings, Long> {
    List<AssetsSettings> findBySettingType(String settingType);
}
