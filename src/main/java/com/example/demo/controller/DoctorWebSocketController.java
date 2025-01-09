package com.example.demo.controller;

import com.example.demo.DTO.CaseIdMessageDTO;
import com.example.demo.DTO.TemporaryFileDTO;
import com.example.demo.DTO.WebSocketDepartmentTemporaryFileDTO;
import com.example.demo.DTO.WebSocketDoctorResponse;
import com.example.demo.service.CaseService;
import com.example.demo.service.TemporaryFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@EnableScheduling
public class DoctorWebSocketController {

    @Autowired
    private TemporaryFileService temporaryFileService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/department/{departmentName}")
    @SendTo("/topic/department/{departmentName}")
    public WebSocketDoctorResponse departmentMessage(@Payload CaseIdMessageDTO message, @DestinationVariable String departmentName, SimpMessageHeaderAccessor headerAccessor) {

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        Long id = message.getId();

        Long returnId =caseService.handleTemporaryFile(id, userId);
        return new WebSocketDoctorResponse(returnId);
    }

    @Scheduled(fixedRate = 300000)
    public void sendPatientFiles() {
        List<WebSocketDepartmentTemporaryFileDTO> temporaryFiles = temporaryFileService.getTemporaryFilesWithNotNullDepartment();

        Map<String, List<WebSocketDepartmentTemporaryFileDTO>> departments = temporaryFiles.stream()
                .collect(Collectors.groupingBy(
                        WebSocketDepartmentTemporaryFileDTO::getDepartment
                ));

        departments.forEach((department, files) -> {
            messagingTemplate.convertAndSend("/topic/doctor/" + department, files);
        });
    }


}
