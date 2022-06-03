package com.karim.service;

import com.karim.model.Patient;
import com.karim.repository.PatientRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepostory patientRepostory;

    public Patient createPatient(Patient patient){
        return patientRepostory.save(patient);
    }

    public List<Patient> getAllPatients(){
        return patientRepostory.findAll();
    }

    public Patient getPatientById(String id ){
        return patientRepostory.findById(id);
    }

    public Patient updatePatient(String id , Patient newPatient){
        Patient patient = getPatientById(id);
        patient.setFirstName(newPatient.getFirstName());
        patient.setLastName(newPatient.getLastName());
        patient.setEmail(newPatient.getEmail());
        patient.setPhone(newPatient.getPhone());
        patient.setAddress(newPatient.getAddress());
        return patientRepostory.saveAndFlush(patient);
    }

    public void deletePatient(String id){
        patientRepostory.deleteById(id);
    }



}
