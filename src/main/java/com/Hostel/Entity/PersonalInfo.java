package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "personal_info")
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalInfoId;

    @Size(max = 100)
    @Column(nullable = true)
    private String fullName;

    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private Integer age;

    @Size(max = 20)
    @Column(nullable = true)
    private String gender;

    @Size(max = 20)
    @Column(nullable = true)
    private String maritalStatus;

    @Size(max = 10)
    @Column(nullable = true)
    private String bloodGroup;

    @Size(max = 50)
    @Column(nullable = true)
    private String religion;

    @Column(length = 255, nullable = true)
    private String personalPhoto;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Transient // This field won't be persisted in the database
    private String confirmPassword;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id", nullable = false)
    private HostelForm hostelForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
}