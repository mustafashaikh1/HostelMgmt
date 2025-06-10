package com.Hostel.ServiceImpl;

import com.Hostel.Entity.Admin;
import com.Hostel.Entity.HostelUser;
import com.Hostel.Entity.PersonalInfo;
import com.Hostel.Entity.Reception;
import com.Hostel.Repository.AdminRepository;
import com.Hostel.Repository.HostelUserRepository;
import com.Hostel.Repository.ReceptionRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final ReceptionRepository receptionRepository;
    private final HostelUserRepository hostelUserRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository,
                                  ReceptionRepository receptionRepository,
                                  HostelUserRepository hostelUserRepository) {
        this.adminRepository = adminRepository;
        this.receptionRepository = receptionRepository;
        this.hostelUserRepository = hostelUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if user is an admin
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null) {
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // Check if user is a reception
        Reception reception = receptionRepository.findByEmail(username);
        if (reception != null) {
            return new User(
                    reception.getEmail(),
                    reception.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_RECEPTION"))
            );
        }

        HostelUser hostelUser = hostelUserRepository.findByEmail(username).orElse(null);
        if (hostelUser != null) {
            return new User(
                    hostelUser.getEmail(),
                    hostelUser.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        // Check if user exists in PersonalInfo
        Optional<PersonalInfo> personalInfo = hostelUserRepository.findPersonalInfoByEmail(username);
        if (personalInfo.isPresent()) {
            return new User(
                    personalInfo.get().getEmail(),
                    personalInfo.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}