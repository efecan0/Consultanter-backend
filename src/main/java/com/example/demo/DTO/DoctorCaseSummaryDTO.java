package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCaseSummaryDTO {
    private Long doctorId;
    private String firstName;
    private String lastName;
    private Long reviewedCase;
    private Long caseCount;
}
