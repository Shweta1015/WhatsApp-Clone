package com.WhatsappClone.Services;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.MessageException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Chat;
import com.WhatsappClone.Modal.Message;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Repository.ChatRepository;
import com.WhatsappClone.Repository.MessageRepository;
import com.WhatsappClone.Request.SendMessageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService{
    private MessageRepository messageRepository;
    private ChatService chatService;
    private UserService userService;

    public MessageServiceImplementation(MessageRepository messageRepository, ChatService chatService, UserService userService){
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setDateTime(LocalDateTime.now());

        return message;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);

        if(!chat.getUsers().contains(reqUser)){
            throw new UserException("You do not relate to this chat "+chat.getId());
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> optional = messageRepository.findById(messageId);

        if(optional.isPresent()){
            return optional.get();
        }
        throw new MessageException("message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {

        Message message = findMessageById(messageId);

        if(message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You can't delete another user's message"+reqUser.getName());
    }
}
