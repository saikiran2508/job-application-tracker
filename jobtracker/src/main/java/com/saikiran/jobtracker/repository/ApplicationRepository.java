package com.saikiran.jobtracker.repository;

import com.saikiran.jobtracker.model.Application;
import com.saikiran.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);

    Optional<Application> findByIdAndUser(Long id, User user);
}