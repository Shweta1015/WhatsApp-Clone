package com.WhatsappClone.Services;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Chat;
import com.WhatsappClone.Modal.User;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface ChatService {

    public Chat createChat(User reqUser, Integer userId1) throws UserException;   //(user, person to chat with)
    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    public Chat creatGroup(GroupChatRequest req, User reqUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException;
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;
    public void deleteChat(Integer chatId, Integer userid) throws ChatException, UserException;

}
