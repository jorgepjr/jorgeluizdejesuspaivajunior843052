package com.musiccatalog.repository;

import com.musiccatalog.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Long> {
    Optional<Regional> findBySincId(String sincId);
}
