package com.Hostel.Controller;
import com.Hostel.Dto.Request.LoginRequest;
import com.Hostel.Dto.Response.JwtResponse;
import com.Hostel.Service.HostelUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/hostelUsers")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class HostelUserController {

    @Autowired
    private HostelUserService hostelUserService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = hostelUserService.loginWithPersonalInfo(loginRequest);
        return ResponseEntity.ok(response);
    }
}