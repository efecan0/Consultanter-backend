package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDtoIU {
    private String email;
    private String name;
    private String surname;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String country;
    private String city;
}