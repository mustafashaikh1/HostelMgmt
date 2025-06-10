package com.Hostel.ServiceImpl;



import com.Hostel.Entity.HostelFooter;
import com.Hostel.Repository.HostelFooterRepository;
import com.Hostel.Service.HostelFooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostelFooterServiceImpl implements HostelFooterService {

    @Autowired
    private HostelFooterRepository hostelFooterRepository;

    @Override
    public HostelFooter saveFooter(HostelFooter footer) {
        return hostelFooterRepository.save(footer);
    }

    @Override
    public HostelFooter updateFooter(Long footerId, HostelFooter updatedFooter) {
        HostelFooter footer = hostelFooterRepository.findById(footerId)
                .orElseThrow(() -> new RuntimeException("Footer not found with ID: " + footerId));

        // Update fields
        footer.setTitle(updatedFooter.getTitle());
        footer.setFooterColor(updatedFooter.getFooterColor());
        footer.setEmail(updatedFooter.getEmail());
        footer.setMobileNumber(updatedFooter.getMobileNumber());
        footer.setAddress(updatedFooter.getAddress());
        footer.setInstagramLink(updatedFooter.getInstagramLink());
        footer.setFacebookLink(updatedFooter.getFacebookLink());
        footer.setTwitterLink(updatedFooter.getTwitterLink());
        footer.setYoutubeLink(updatedFooter.getYoutubeLink());
        footer.setWhatsappLink(updatedFooter.getWhatsappLink());

        return hostelFooterRepository.save(footer);
    }

    @Override
    public void deleteFooter(Long footerId) {
        if (!hostelFooterRepository.existsById(footerId)) {
            throw new RuntimeException("Footer not found with ID: " + footerId);
        }
        hostelFooterRepository.deleteById(footerId);
    }

    @Override
    public Optional<HostelFooter> getFooterById(Long footerId) {
        return hostelFooterRepository.findById(footerId);
    }

    @Override
    public List<HostelFooter> getAllFooters() {
        return hostelFooterRepository.findAll();
    }

}
