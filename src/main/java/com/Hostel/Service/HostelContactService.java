package com.Hostel.Service;

import com.Hostel.Entity.HostelContact;

import java.util.List;

public interface HostelContactService {
    HostelContact saveHostelContact(HostelContact hostelContact);
    HostelContact getHostelContactById(Long id);
    List<HostelContact> getAllHostelContacts();
    HostelContact updateHostelContact(Long id, HostelContact hostelContact);
    void deleteHostelContact(Long id);
}
