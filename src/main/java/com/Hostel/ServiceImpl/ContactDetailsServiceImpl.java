package com.Hostel.ServiceImpl;

import com.Hostel.Entity.ContactDetails;
import com.Hostel.Entity.HostelForm;
import com.Hostel.Repository.ContactDetailsRepository;
import com.Hostel.Repository.HostelFormRepository;
import com.Hostel.Service.ContactDetailsService;
import com.Hostel.Service.HostelFormService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContactDetailsServiceImpl implements ContactDetailsService {

    @Autowired
    private ContactDetailsRepository contactDetailsRepository;

    @Autowired
    private HostelFormService hostelFormService;

    @Autowired
    private HostelFormRepository hostelFormRepository;

    @Override
    public ContactDetails saveContactDetails(ContactDetails contactDetails, String formNumber) {
        // Fetch the HostelForm by formNumber
        HostelForm hostelForm = hostelFormService.getHostelFormByFormNumber(formNumber);

        if (hostelForm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HostelForm not found with formNumber: " + formNumber);
        }

        // Initialize lazy-loaded fields manually
        Hibernate.initialize(hostelForm.getPersonalInfo());
        Hibernate.initialize(hostelForm.getFamilyDetails());

        // Set the HostelForm to the ContactDetails object
        contactDetails.setHostelForm(hostelForm);

        // Save the ContactDetails
        ContactDetails savedContactDetails = contactDetailsRepository.save(contactDetails);

        // Update the HostelForm with the saved ContactDetails
        hostelForm.setContactDetails(savedContactDetails);
        hostelFormService.saveHostelForm(hostelForm);

        return savedContactDetails;
    }

    @Override
    public List<ContactDetails> getAllContactDetails() {
        List<ContactDetails> contactDetailsList = contactDetailsRepository.findAll();
        if (contactDetailsList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Contact Details found.");
        }
        return contactDetailsList;
    }

    @Override
    public ContactDetails getContactDetailsById(Long contactId) {
        return contactDetailsRepository.findById(contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ContactDetails not found with ID: " + contactId));
    }

    @Override
    public ContactDetails updateContactDetails(Long contactId, ContactDetails contactDetails) {
        ContactDetails existingContactDetails = getContactDetailsById(contactId);

        existingContactDetails.setAddress(contactDetails.getAddress());
        existingContactDetails.setCity(contactDetails.getCity());
        existingContactDetails.setState(contactDetails.getState());
        existingContactDetails.setPincode(contactDetails.getPincode());
        existingContactDetails.setMobileNo(contactDetails.getMobileNo());
        existingContactDetails.setEmail(contactDetails.getEmail());

        return contactDetailsRepository.save(existingContactDetails);
    }

    @Override
    public void deleteContactDetails(Long contactId) {
        if (!contactDetailsRepository.existsById(contactId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ContactDetails not found with ID: " + contactId);
        }
        contactDetailsRepository.deleteById(contactId);
    }
}
