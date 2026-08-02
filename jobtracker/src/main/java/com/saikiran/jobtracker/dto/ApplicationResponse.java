package com.saikiran.jobtracker.dto;

import com.saikiran.jobtracker.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private String companyName;
    private String roleTitle;
    private String jobLink;
    private ApplicationStatus status;
    private LocalDate dateApplied;
}