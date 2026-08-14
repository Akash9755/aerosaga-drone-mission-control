package com.example.aerosaga.repository;

import com.example.aerosaga.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByStatus(Mission.MissionStatus status);

    List<Mission> findByDroneId(Long droneId);
}