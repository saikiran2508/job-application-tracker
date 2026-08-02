package com.saikiran.jobtracker.service;

import com.saikiran.jobtracker.model.Application;
import com.saikiran.jobtracker.model.User;
import com.saikiran.jobtracker.repository.ApplicationRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public ApplicationService(ApplicationRepository applicationRepository, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(email);
    }

    public Application createApplication(Application application) {
        application.setUser(getCurrentUser());
        return applicationRepository.save(application);
    }

    public List<Application> getMyApplications() {
        return applicationRepository.findByUser(getCurrentUser());
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    public Application updateStatus(Long id, Application updatedFields) {
        Application application = getApplicationById(id);
        application.setStatus(updatedFields.getStatus());
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }
}