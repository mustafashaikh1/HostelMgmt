package com.Hostel.ServiceImpl;

import com.Hostel.Entity.HostelContact;
import com.Hostel.Repository.HostelContactRepository;
import com.Hostel.Service.HostelContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostelContactServiceImpl implements HostelContactService {

    @Autowired
    private HostelContactRepository hostelContactRepository;

    @Override
    public HostelContact saveHostelContact(HostelContact hostelContact) {
        return hostelContactRepository.save(hostelContact);
    }

    @Override
    public HostelContact getHostelContactById(Long id) {
        return hostelContactRepository.findById(id).orElse(null);
    }

    @Override
    public List<HostelContact> getAllHostelContacts() {
        return hostelContactRepository.findAll();
    }

    @Override
    public HostelContact updateHostelContact(Long id, HostelContact hostelContact) {
        Optional<HostelContact> existingContact = hostelContactRepository.findById(id);
        if (existingContact.isPresent()) {
            hostelContact.setId(id);
            return hostelContactRepository.save(hostelContact);
        }
        return null;
    }

    @Override
    public void deleteHostelContact(Long id) {
        hostelContactRepository.deleteById(id);
    }
}
