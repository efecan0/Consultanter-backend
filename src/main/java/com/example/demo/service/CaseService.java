package com.example.demo.service;

import com.example.demo.DTO.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaseService {

    @Autowired
    private TemporaryFileRepository temporaryFileRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RateRepository rateRepository;

    @Transactional
    public Long handleTemporaryFile(Long caseId, Long docId) {
        TemporaryFile temporaryFile = temporaryFileRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("TemporaryFile not found for ID: " + caseId));

        User doctor = userRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for ID: " + docId));


        Case newCase = new Case.Builder()
                .setId(temporaryFile.getId())
                .setComplaint(temporaryFile.getComplaint())
                .setPatient(temporaryFile.getPatient())
                .setDoctor(doctor)
                .setDate(temporaryFile.getDate())
                .setHeight(temporaryFile.getHeight())
                .setWeight(temporaryFile.getWeight())
                .setKnownConditions(temporaryFile.getKnownConditions())
                .setDepartment(temporaryFile.getDepartment())
                .setDetailedDiagnosis()
                .setSummaryDiagnosis()
                .build();

        caseRepository.save(newCase);

        temporaryFileRepository.delete(temporaryFile);

        return caseId;
    }

    public List<CaseProfileDTO> mineCasesAsPatient(Long id) {
        Optional<User> patient = userRepository.findById(id);
        if (patient.isPresent()) {
            User patient_now = patient.get();

            return caseRepository.findMineCasesAsDTOAsPatient(patient_now);

        } else {
            throw new EntityNotFoundException("Patient not found with id: " + id);
        }

    }

    public List<CaseProfileDTO> mineCasesAsDoctor(Long id) {
        Optional<User> doctor = userRepository.findById(id);
        if(doctor.isPresent()) {
            User doctor_now = doctor.get();
            return caseRepository.findMineCasesAsDTOAsDoctor(doctor_now);
        }else {
            throw new EntityNotFoundException("Doctor not found with id: " + id);
        }
    }


    @Transactional
    public CaseDetailDTO getCaseDetail(Long id) {
        Case caseDetail = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TemporaryFile kaydı bulunamadı"));


        List<FileDocument> documents = fileDocumentRepository.findByFileIdAndType(id, "document");

        List<FileDocumentDTO> documentDTOs = documents.stream()
                .map(doc -> new FileDocumentDTO(
                        doc.getId(),
                        doc.getFileId(),
                        doc.getType(),
                        doc.getFileData()
                ))
                .collect(Collectors.toList());

        CaseDetailDTO detailDTO = new CaseDetailDTO();
        detailDTO.setComplaint(caseDetail.getComplaint());
        detailDTO.setDate(caseDetail.getDate());
        detailDTO.setHeight(caseDetail.getHeight());
        detailDTO.setWeight(caseDetail.getWeight());
        detailDTO.setKnownConditions(caseDetail.getKnownConditions());
        detailDTO.setDepartment(caseDetail.getDepartment());
        detailDTO.setDocuments(documentDTOs);

        return detailDTO;
    }

    public void commentCase(Long sessionId, Long caseId , String message){
        //patientId , doctorId
        Case dbCase = caseRepository.findById(caseId).orElseThrow(() -> new RuntimeException("Dosya bulunamadı."));
        if(dbCase.getPatient().getId() == sessionId){
            Comment comment = new Comment();
            comment.setMessage(message);
            commentRepository.save(comment);

            Doctor caseDoctor = doctorRepository.findById(dbCase.getDoctor().getId()).orElseThrow(() -> new RuntimeException("Dosya ile ilgili doktor bulunamadı."));
            caseDoctor.addComment(comment);
            doctorRepository.save(caseDoctor);

            dbCase.setComment(comment);
            caseRepository.save(dbCase);
            return;

        }

        throw new RuntimeException("Dosyanın sahibi bu kullanıcı değil.");
    }

    public void rateCase(Long sessionId , Long caseId , Short rating){
        Case dbCase = caseRepository.findById(caseId).orElseThrow(() -> new RuntimeException("Dosya bulunamadı."));

        if(dbCase.getPatient().getId() == sessionId){
            Rate rate = new Rate();
            rate.setRating(rating);
            rateRepository.save(rate);

            dbCase.setRate(rate);
            caseRepository.save(dbCase);

            Doctor caseDoctor = doctorRepository.findById(dbCase.getDoctor().getId()).orElseThrow(() -> new RuntimeException("Dosya ile ilgili doktor bulunamadı."));
            caseDoctor.addRate(rate);
            doctorRepository.save(caseDoctor);
            return;
        }

        throw new RuntimeException("Dosyanın sahibi bu kullanıcı değil.");
    }

}
