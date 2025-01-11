package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaintId;

    @Column(insertable=false, updatable=false, name = "user_type")
    private String userType;

    public Message(String message, Complaint complaintId, String userType) {
        this.message = message;
        this.complaintId = complaintId;
        this.userType = userType;
    }

}
