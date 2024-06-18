package com.WhatsappClone.Modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String content;

    private LocalDateTime dateTime;
    @ManyToOne           //one user multiple msgs
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatId")
    private Chat chat;

}
