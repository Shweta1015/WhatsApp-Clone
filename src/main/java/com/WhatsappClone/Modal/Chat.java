package com.WhatsappClone.Modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chatName;
    private String chatImage;

    @ManyToMany
    private Set<User> admin = new HashSet<>();

    @Column(name = "isGroup")
    private Boolean isGroup;

    @JoinColumn(name = "createdBy")
    @ManyToOne                 //multiple chat  created by one user
    private User createdBy;

    @ManyToMany               //multiple user can create multiple chat
    private Set<User> users = new HashSet<>();


    @OneToMany                      //multiple msgs for one chat
    private List<Message> messages =new ArrayList<>();


    // to use Set<User>
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(chatName, chat.chatName) && Objects.equals(chatImage, chat.chatImage) && Objects.equals(isGroup, chat.isGroup) && Objects.equals(createdBy, chat.createdBy) && Objects.equals(users, chat.users) && Objects.equals(messages, chat.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatName, chatImage, isGroup, createdBy, users, messages);
    }
}
