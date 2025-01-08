package com.example.demo.repository;

import com.example.demo.DTO.DoctorUserDTO;
import com.example.demo.DTO.DoctorUserFullDTO;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmail(String email);

    /*
    @Query("SELECT d.profilePhoto FROM Doctor d WHERE d.id = :doctorId")
    byte[] findProfilePhotoById(@Param("doctorId") Long doctorId);
     */

    @Query("SELECT new com.example.demo.DTO.DoctorUserDTO(d.id, u.email, u.name, u.surname) " +
            "FROM Doctor d JOIN d.user u " +
            "WHERE d.adminApproval = false")
    List<DoctorUserDTO> findByAdminApprovalFalse();

    @Query("SELECT new com.example.demo.DTO.DoctorUserFullDTO(u.email, u.name, u.surname, u.phone, u.birthDate, " +
            "u.gender, u.country, u.city, d.specialization, d.adminApproval, d.profilePhoto, d.degreePhoto, d.certificatePhoto, d.taxPlate) " +
            "FROM Doctor d JOIN d.user u " +
            "WHERE d.id = :id")
    DoctorUserFullDTO findDoctorFullDetailsById(@Param("id") Long id);

}
