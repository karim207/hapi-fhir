package com.karim.service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.karim.model.Address;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FhirPatient {

    @Autowired
    private IGenericClient client;

    public List<Patient> getAllPatients(){
        Bundle bundle = client.search()
            .forResource(Patient.class)
            .returnBundle(Bundle.class)
            .execute();
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < bundle.getEntry().size(); i++) {
            patients.add((Patient) bundle.getEntry().get(i).getResource());
        }
        return patients;
    }
    public Patient getPatientById(String id){
        Patient patient =  client.read()
            .resource(Patient.class)
            .withId(id).execute();
        return patient;
    }

    public String createPatienInFhir(com.karim.model.Patient patient){
        Patient newPatient = new Patient();
        newPatient.addName().addGiven(patient.getFirstName()).setFamily(patient.getLastName());
        newPatient.addAddress().addLine(patient.getAddress().getLine())
            .setCity(patient.getAddress().getCity())
            .setCountry(patient.getAddress().getState())
            .setPostalCode(patient.getAddress().getPostCode());
        newPatient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setUse(ContactPoint.ContactPointUse.HOME).setValue(patient.getPhone());
        newPatient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL)
            .setValue(patient.getEmail());
        MethodOutcome methodOutcome = client.create().resource(newPatient).execute();
        IIdType idType = methodOutcome.getId();
        String[] split = idType.toString().split("/");
        String id = split[5];
        return id ;
    }

    public void updatePatient(String id , com.karim.model.Patient newPatient){
        Patient patient = getPatientById(id);

        patient.getName().get(0)
            .setFamily(newPatient.getLastName())
            .getGiven().set(0 , new StringType(newPatient.getFirstName()));
        patient.getTelecom().get(0).setValue(newPatient.getPhone());
        patient.getTelecom().get(1).setValue(newPatient.getEmail());
        patient.getAddress().get(0).setCountry(newPatient.getAddress().getState())
            .setPostalCode(newPatient.getAddress().getPostCode())
            .setCity(newPatient.getAddress().getCity())
            .getLine().set(0 , new StringType(newPatient.getAddress().getLine()));

        System.out.println(patient.getName().get(0).getFamily());
        client.update().resource(patient).execute();
    }

    public com.karim.model.Patient createPatient(Patient patient , String id){
        com.karim.model.Patient newPatient = new com.karim.model.Patient();
        // create patient's address
        Address address = new Address();
        address.setLine(String.valueOf(patient.getAddress().get(0).getLine().get(0)));
        address.setCity(patient.getAddress().get(0).getCity());
        address.setState(patient.getAddress().get(0).getState());
        address.setPostCode(patient.getAddress().get(0).getPostalCode());

        newPatient.setId(id);
        newPatient.setFirstName(String.valueOf(patient.getName().get(0).getGiven().get(0)));
        newPatient.setLastName(patient.getName().get(0).getFamily());
        newPatient.setPhone(patient.getTelecom().get(0).getValue());
        newPatient.setEmail(patient.getTelecom().get(1).getValue());
        newPatient.setAddress(address);

        return newPatient;
    }


    public void deletePatientFromFhir(String id){
        Patient patient = getPatientById(id);
        client.delete().resource(patient).execute();
    }

}
