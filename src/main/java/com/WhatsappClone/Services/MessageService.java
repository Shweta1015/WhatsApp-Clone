package com.WhatsappClone.Services;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.MessageException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Message;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Request.SendMessageRequest;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;

    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;
}
