package com.example.demo.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetailDTO {

    private String complaint;

    private LocalDate date;

    private Double height;

    private Double weight;

    private String knownConditions;

    private String department;

    private String summaryDiagnosis;

    private String detailedDiagnosis;

    private boolean requiresSecondOpinion;

    private List<FileDocumentDTO> documents;

}
