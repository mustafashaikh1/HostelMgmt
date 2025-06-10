package com.Hostel.Dto;


import com.Hostel.Entity.Reception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDTO {
    private Long id;
    private String email;
    private String username;
    private String mobileNumber;
    private List<Reception> receptions; // Include reception details if needed
}
