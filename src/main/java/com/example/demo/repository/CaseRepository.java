package com.example.demo.repository;

import com.example.demo.DTO.CaseProfileDTO;
import com.example.demo.model.Case;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Long> {

    @Query("SELECT new com.example.demo.DTO.CaseProfileDTO(d.id, d.complaint, d.date) FROM Case d WHERE d.patient = :patientId")
    List<CaseProfileDTO> findMineCasesAsDTOAsPatient(@Param("patientId") User patient);


    @Query("SELECT new com.example.demo.DTO.CaseProfileDTO(d.id, d.complaint, d.date) FROM Case d WHERE d.doctor = :doctorId")
    List<CaseProfileDTO> findMineCasesAsDTOAsDoctor(@Param("doctorId") User doctor);

}
