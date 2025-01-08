package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseDiagnosisDtoIU {
    private String summaryDiagnosis;

    private String detailedDiagnosis;
}
