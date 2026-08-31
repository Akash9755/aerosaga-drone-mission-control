package com.example.aerosaga.repository;

import com.example.aerosaga.entity.Drone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findByStatus(Drone.DroneStatus status);

    Page<Drone> findByStatus(Drone.DroneStatus status, Pageable pageable);
}