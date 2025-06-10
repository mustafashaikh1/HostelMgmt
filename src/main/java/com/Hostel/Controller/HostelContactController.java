package com.Hostel.Controller;

import com.Hostel.Entity.HostelContact;
import com.Hostel.Service.HostelContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelContactController {

    @Autowired
    private HostelContactService hostelContactService;

    @PostMapping("/createHostelContact")
    public HostelContact createHostelContact(@RequestBody HostelContact hostelContact) {
        return hostelContactService.saveHostelContact(hostelContact);
    }

    @GetMapping("/getHostelContact/{id}")
    public HostelContact getHostelContact(@PathVariable Long id) {
        return hostelContactService.getHostelContactById(id);
    }

    @GetMapping("/getAllHostelContacts")
    public List<HostelContact> getAllHostelContacts() {
        return hostelContactService.getAllHostelContacts();
    }

    @PutMapping("/updateHostelContact/{id}")
    public HostelContact updateHostelContact(@PathVariable Long id, @RequestBody HostelContact hostelContact) {
        return hostelContactService.updateHostelContact(id, hostelContact);
    }

    @DeleteMapping("/deleteHostelContact/{id}")
    public void deleteHostelContact(@PathVariable Long id) {
        hostelContactService.deleteHostelContact(id);
    }
}
