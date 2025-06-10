package com.Hostel.Service;

import com.Hostel.Entity.ContactDetails;

import java.util.List;

public interface ContactDetailsService {

    // Method to save ContactDetails
    ContactDetails saveContactDetails(ContactDetails contactDetails, String formNumber);

    // Method to get all ContactDetails
    List<ContactDetails> getAllContactDetails();

    // Method to get ContactDetails by ID
    ContactDetails getContactDetailsById(Long contactId);

    // Method to update ContactDetails
    ContactDetails updateContactDetails(Long contactId, ContactDetails contactDetails);

    // Method to delete ContactDetails by ID
    void deleteContactDetails(Long contactId);
}
