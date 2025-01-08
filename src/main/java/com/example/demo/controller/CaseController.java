package com.example.demo.controller;

import com.example.demo.DTO.CaseDiagnosisDtoIU;
import com.example.demo.DTO.CaseProfileDTO;
import com.example.demo.service.CaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/case")
@PreAuthorize("@dashboardController.isLoggedIn()")
public class CaseController {

    @Autowired
    CaseService caseService;

    @GetMapping("/mine")
    public ResponseEntity<Map<String, Object>> mineCases(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

            Long userId = (Long) session.getAttribute("userId");
            String userType = (String) session.getAttribute("userType");

            List<CaseProfileDTO> cases;

            if("DOCTOR".equals(userType)) {
                cases = caseService.mineCasesAsDoctor(userId);
            }else if("PATIENT".equals(userType)) {
                cases = caseService.mineCasesAsPatient(userId);
            }else {
                throw new IllegalArgumentException("Invalid user type: " + userType);
            }


            response.put("success", true);
            response.put("data", cases);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);



    }

    @PostMapping("comment/{id}")
    public ResponseEntity<Map<String , Object>> commentCase(HttpSession session , @PathVariable("id") Long caseId, @RequestBody String message){
        Long sessionId = (Long)session.getAttribute("userId");
        String sessionType = (String)session.getAttribute("userType");
        Map<String , Object> response = new HashMap<>();
        if (sessionType.equals("PATIENT")){
            caseService.commentCase(sessionId ,caseId , message);


            response.put("success" , true);
            response.put("message" , "comment is succesfully send.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("success" , false);
        response.put("message" , "You are not a patient.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("rate/{id}")
    public ResponseEntity<Map<String , Object>> rateCase(HttpSession session , @PathVariable(name = "id") Long caseId, @RequestBody Short rating){
        Long sessionId = (Long)session.getAttribute("userId");
        String sessionType = (String)session.getAttribute("userType");
        Map<String , Object> response = new HashMap<>();
        if (sessionType.equals("PATIENT")){
            caseService.rateCase(sessionId ,caseId , rating);

            response.put("success" , true);
            response.put("message" , "Rate is succesfully send.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("success" , false);
        response.put("message" , "You are not a patient.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }



}
