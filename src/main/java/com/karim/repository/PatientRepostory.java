package com.karim.repository;

import com.karim.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepostory extends JpaRepository<com.karim.model.Patient , Long> {

    Patient findById(String id);
    void deleteById(String id);
}
