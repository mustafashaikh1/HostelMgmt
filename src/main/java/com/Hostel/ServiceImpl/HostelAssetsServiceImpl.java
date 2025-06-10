package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelAssets;
import com.Hostel.Repository.HostelAssetsRepository;
import com.Hostel.Service.HostelAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelAssetsServiceImpl implements HostelAssetsService {

    @Autowired
    private HostelAssetsRepository assetsRepository;

    @Override
    public HostelAssets createAsset(HostelAssets asset) {
        try {
            return assetsRepository.save(asset);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Model number already exists: " + asset.getModelNumber());
        }}



    @Override
    public HostelAssets updateAsset(Long id, HostelAssets asset) {
        HostelAssets existing = assetsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        // Check for duplicate model number (if changed)
        if (!existing.getModelNumber().equals(asset.getModelNumber())) {
            if (assetsRepository.existsByModelNumber(asset.getModelNumber())) {
                throw new RuntimeException("Model number already exists: " + asset.getModelNumber());
            }
        }

        // update all fields
        existing.setAssetType(asset.getAssetType());
        existing.setAssetCategory(asset.getAssetCategory());
        existing.setDateOfPurchase(asset.getDateOfPurchase());
        existing.setAssetName(asset.getAssetName());
        existing.setModelNumber(asset.getModelNumber());
        existing.setPurchasePrice(asset.getPurchasePrice());
        existing.setEstimatePrice(asset.getEstimatePrice());
        existing.setExpiryDate(asset.getExpiryDate());
        existing.setPurchasedBy(asset.getPurchasedBy());
        existing.setManagedBy(asset.getManagedBy());
        existing.setAssetStatus(asset.getAssetStatus());
        existing.setQuantity(asset.getQuantity());

        return assetsRepository.save(existing);
    }

    @Override
    public void deleteAsset(Long id) {
        assetsRepository.deleteById(id);
    }

    @Override
    public HostelAssets getAssetById(Long id) {
        return assetsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Override
    public List<HostelAssets> getAllAssets() {
        return assetsRepository.findAll();
    }
}
