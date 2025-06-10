package com.Hostel.Service;

import com.Hostel.Entity.HostelAssets;

import java.util.List;

public interface HostelAssetsService {
    HostelAssets createAsset(HostelAssets asset);
    HostelAssets updateAsset(Long id, HostelAssets asset);
    void deleteAsset(Long id);
    HostelAssets getAssetById(Long id);
    List<HostelAssets> getAllAssets();
}
