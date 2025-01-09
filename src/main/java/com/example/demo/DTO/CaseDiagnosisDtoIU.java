package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseDiagnosisDtoIU {
    private String summaryDiagnosis;

    private String detailedDiagnosis;

    private LocalDate date;
}
