package com.karim.controller;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.karim.model.Address;
import com.karim.service.FhirPatient;
import com.karim.service.PatientService;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Patient")
public class PatientController {

    @Autowired
    private IGenericClient client;
    @Autowired
    private IParser parser;

    @Autowired
    private FhirPatient patient;

    @Autowired
    private PatientService patientService;




    @PostMapping()  // save Patient into fhir and database
    public com.karim.model.Patient createNewPatient(@RequestBody com.karim.model.Patient mypatient){
        String id = patient.createPatienInFhir(mypatient);
        mypatient.setId(id);
        return patientService.createPatient(mypatient) ;
    }

    @PostMapping("/{id}")   // get Patient from fhir and save it in database
    public com.karim.model.Patient createPatientFromFhir(@PathVariable String id){
        Patient getPatient = patient.getPatientById(id);
        com.karim.model.Patient newPatient = patient.createPatient(getPatient , id);
        return patientService.createPatient(newPatient);
    }

    @GetMapping()   // get All Patient from database
    public List<com.karim.model.Patient> getAllPatient(){
        List<com.karim.model.Patient> patients = patientService.getAllPatients();
        return patients;
    }

    @GetMapping("/fhir/{id}")    // get Patient by id from fihr
    public String findPatientFromFhirById(@PathVariable String id){
        Patient response = patient.getPatientById(id);
        return parser.encodeResourceToString(response);
    }

    @GetMapping("/{id}")        // get Patient by id from database
    public com.karim.model.Patient findPatientById(@PathVariable String id){
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public com.karim.model.Patient updatePatient
        (@PathVariable String id , @RequestBody com.karim.model.Patient newPatient){
        patient.updatePatient(id , newPatient);
        com.karim.model.Patient updatedPatient1 =  patientService.updatePatient(id , newPatient);
        return updatedPatient1 ;
    }


    @DeleteMapping("/{id}")
    public void deletePatientById(@PathVariable String id){
        patient.deletePatientFromFhir(id);
        patientService.deletePatient(id);
        System.out.println("patient by id +"+ id + "is deleted successfully!!!");
    }


}
