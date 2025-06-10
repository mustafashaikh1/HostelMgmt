package com.Hostel.ServiceImpl;

import com.Hostel.Dto.Request.LoginRequest;
import com.Hostel.Dto.Response.JwtResponse;
import com.Hostel.Entity.PersonalInfo;
import com.Hostel.Repository.HostelUserRepository;
import com.Hostel.Service.HostelUserService;
import com.Hostel.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HostelUserServiceImpl implements HostelUserService {

    @Autowired
    private HostelUserRepository hostelUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // Update HostelUserServiceImpl.java
    @Override
    public JwtResponse loginWithPersonalInfo(LoginRequest loginRequest) {
        // Find the personal info by email
        Optional<PersonalInfo> personalInfoOpt = hostelUserRepository.findPersonalInfoByEmail(loginRequest.getEmail());

        if (personalInfoOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail());
        }

        PersonalInfo personalInfo = personalInfoOpt.get();

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // If authentication is successful, generate token
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(loginRequest.getEmail(), "USER");

            // Get the form number from the associated hostel form
            String formNumber = null;
            if (personalInfo.getHostelForm() != null) {
                formNumber = personalInfo.getHostelForm().getFormNumber();
            }

            return JwtResponse.builder()
                    .token(token)
                    .id(personalInfo.getPersonalInfoId())
                    .email(personalInfo.getEmail())
                    .username(personalInfo.getFullName())
                    .message("Login successful")
                    .formNumber(formNumber)  // Include form number in response
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user credentials");
        }
    }

    @Override
    public PersonalInfo findPersonalInfoByEmail(String email) {
        return hostelUserRepository.findPersonalInfoByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}