package com.Hostel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "UserActivity")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userActivityId;  // Rename id to userActivityId

    private String activityType; // "study" or "work"




    // One-to-One relationship with StudyDetails
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "study_details_id")
    private StudyDetails studyDetails;

    // One-to-One relationship with WorkDetails
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_details_id")
    private WorkDetails workDetails;

    @JsonBackReference // Add this to break recursion on the child side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_form_id")
    private HostelForm hostelForm;


}
