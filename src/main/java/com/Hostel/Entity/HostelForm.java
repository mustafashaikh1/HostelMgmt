package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Table(name = "HostelForm")
public class HostelForm {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long hostelFormId;

  @Column(nullable = false, unique = true)
  private String formNumber;

  @Column(nullable = false)
  private LocalDate date;

  @OneToOne(mappedBy = "hostelForm", cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonManagedReference
  @JoinColumn(name = "personal_info_id")
  private PersonalInfo personalInfo;




  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "family_details_id")
  private FamilyDetails familyDetails;


  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "contact_id" )
  private ContactDetails contactDetails;



  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "guardian_id")
  private LocalGuardianDetails localGuardianDetails;


  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "medical_information_id")
  private MedicalInformation medicalInformation;


  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "user_activity_id", nullable = true)
  private UserActivity userActivity;

 
  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "study_details_id")
  private StudyDetails studyDetails;


  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "work_details_id")
  private WorkDetails workDetails;


  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "vehicle_details_id")
  private VehicleDetails vehicleDetails;


  @JsonManagedReference
  @OneToOne(mappedBy = "hostelForm", fetch = FetchType.LAZY)
  private UndertakingForm undertakingForm;

  @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
  @JoinColumn(name = "office_use_only_id")
  private OfficeUseOnly officeUseOnly;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reception_id", nullable = true)
  @JsonBackReference
  private Reception reception;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Admin admin;


  @OneToMany(mappedBy = "hostelForm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("hostelForm")
  private List<Bed> beds;


  // Add Hostel Agreement relationship
  @OneToOne(mappedBy = "hostelForm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonManagedReference
  private HostelAgreement hostelAgreement;
  @OneToMany(mappedBy = "hostelForm", cascade = CascadeType.ALL)



  @OneToOne(mappedBy = "hostelForm", cascade = CascadeType.ALL)
  @JsonManagedReference
  private DepositeDetails depositeDetails;


  @OneToMany(mappedBy = "hostelForm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("hostelForm")
  private List<LeaveApplication> leaveApplications;


}

