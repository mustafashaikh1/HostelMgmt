package com.Hostel.Service;

import com.Hostel.Dto.Request.LoginRequest;
import com.Hostel.Dto.Response.JwtResponse;
import com.Hostel.Entity.PersonalInfo;

public interface HostelUserService {
    // Authentication methods
    JwtResponse loginWithPersonalInfo(LoginRequest loginRequest);
    PersonalInfo findPersonalInfoByEmail(String email);


}