package com.Hostel.ServiceImpl;


import com.Hostel.Entity.HostelUrlMapping;
import com.Hostel.Exception.HostelUrlMappingException;
import com.Hostel.Repository.HostelUrlMappingRepository;
import com.Hostel.Service.HostelUrlMappingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HostelUrlMappingServiceImpl implements HostelUrlMappingService {

    @Autowired
    private HostelUrlMappingRepository hostelUrlMappingRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final long BACKOFF_DELAY = 1000L; // 1 second delay

    @Override
    @Transactional
    public HostelUrlMapping createHostelUrlMapping(String dynamicPart) throws HostelUrlMappingException {
        return retry(() -> {
            if (dynamicPart == null || dynamicPart.trim().isEmpty()) {
                throw new IllegalArgumentException("Dynamic part cannot be empty");
            }

            hostelUrlMappingRepository.findByDynamicPart(dynamicPart.trim()).ifPresent(mapping -> {
                throw new HostelUrlMappingException("Dynamic part already exists: " + dynamicPart);
            });

            HostelUrlMapping urlMapping = new HostelUrlMapping();
            urlMapping.setDynamicPart(dynamicPart.trim());

            HostelUrlMapping savedMapping = hostelUrlMappingRepository.save(urlMapping);
            log.info("Created Hostel URL mapping: {}", savedMapping);
            return savedMapping;
        });
    }

    @Override
    @Transactional
    public HostelUrlMapping getHostelUrlMapping(String dynamicPart) throws HostelUrlMappingException {
        return retry(() -> {
            if (dynamicPart == null || dynamicPart.trim().isEmpty()) {
                throw new IllegalArgumentException("Dynamic part cannot be empty");
            }
            return hostelUrlMappingRepository.findByDynamicPart(dynamicPart.trim())
                    .orElseThrow(() -> new HostelUrlMappingException("No mapping found for dynamic part: " + dynamicPart));
        });
    }

    @Override
    @Transactional
    public List<HostelUrlMapping> getAllHostelUrlMappings() throws HostelUrlMappingException {
        return retry(hostelUrlMappingRepository::findAll);
    }

    @Override
    @Transactional
    public HostelUrlMapping updateHostelUrlMapping(Long id, String dynamicPart) throws HostelUrlMappingException {
        return retry(() -> {
            if (dynamicPart == null || dynamicPart.trim().isEmpty()) {
                throw new IllegalArgumentException("Dynamic part cannot be empty");
            }

            HostelUrlMapping urlMapping = hostelUrlMappingRepository.findById(id)
                    .orElseThrow(() -> new HostelUrlMappingException("Hostel URL mapping not found with ID: " + id));

            hostelUrlMappingRepository.findByDynamicPart(dynamicPart.trim()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new HostelUrlMappingException("Dynamic part already exists: " + dynamicPart);
                }
            });

            urlMapping.setDynamicPart(dynamicPart.trim());
            HostelUrlMapping updatedMapping = hostelUrlMappingRepository.save(urlMapping);
            log.info("Updated Hostel URL mapping: {}", updatedMapping);
            return updatedMapping;
        });
    }

    @Override
    @Transactional
    public void deleteHostelUrlMapping(Long id) throws HostelUrlMappingException {
        retry(() -> {
            if (!hostelUrlMappingRepository.existsById(id)) {
                throw new HostelUrlMappingException("Hostel URL mapping not found with ID: " + id);
            }
            hostelUrlMappingRepository.deleteById(id);
            log.info("Deleted Hostel URL mapping with ID: {}", id);
            return null;
        });
    }

    private <T> T retry(RetryableAction<T> action) throws HostelUrlMappingException {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                return action.run();
            } catch (DataAccessException e) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    log.error("Failed after {} attempts", attempts);
                    throw new HostelUrlMappingException("Database access error: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(BACKOFF_DELAY);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new HostelUrlMappingException("Failed to perform operation after retries");
    }

    @FunctionalInterface
    public interface RetryableAction<T> {
        T run() throws DataAccessException;
    }
}
