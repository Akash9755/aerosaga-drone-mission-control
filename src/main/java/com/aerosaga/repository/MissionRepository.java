package com.aerosaga.repository;

import com.aerosaga.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByDroneId(Long droneId);
}