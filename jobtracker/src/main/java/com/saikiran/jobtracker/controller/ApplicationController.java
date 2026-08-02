package com.saikiran.jobtracker.controller;

import com.saikiran.jobtracker.dto.ApplicationResponse;
import com.saikiran.jobtracker.model.Application;
import com.saikiran.jobtracker.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@RequestBody Application application) {
        Application saved = applicationService.createApplication(application);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll() {
        List<ApplicationResponse> responses = applicationService.getMyApplications()
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(applicationService.getApplicationById(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long id, @RequestBody Application updatedFields) {
        return ResponseEntity.ok(toResponse(applicationService.updateStatus(id, updatedFields)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    private ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getRoleTitle(),
                application.getJobLink(),
                application.getStatus(),
                application.getDateApplied()
        );
    }
}