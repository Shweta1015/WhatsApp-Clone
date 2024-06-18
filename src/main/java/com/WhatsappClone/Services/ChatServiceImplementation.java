package com.WhatsappClone.Services;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Chat;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService{

    private ChatRepository chatRepository;
    private UserService userService;
    public ChatServiceImplementation(ChatRepository chatRepository, UserService userService){
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    //to create new chat
    @Override
    public Chat createChat(User reqUser, Integer userId1) throws UserException {
        User user = userService.findUserById(userId1);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);

        if(isChatExist != null){
            return isChatExist;
        }

        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setIsGroup(false);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {

        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isPresent()){
            return chat.get();
        }
        throw new ChatException("Chat not found with "+" chatId");
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);

        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats;
    }

    @Override
    public Chat creatGroup(GroupChatRequest req, User reqUser) throws UserException {

        Chat group = new Chat();
        group.setIsGroup(true);
        group.setChatName(req.getChatName());
        group.setChatImage(req.getChatImage());
        group.setCreatedBy(reqUser);
        group.getAdmin().add(reqUser);       //who will create grp becomes admin

        for(Integer userId:req.getUserIds()){
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> optional = chatRepository.findById(chatId);

        User user = userService.findUserById(userId);

        if(optional.isPresent()){
            Chat chat = optional.get();
            if(chat.getAdmin().contains(reqUser)){
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }
            else {
                throw new UserException("Only admin can add user");
            }
        }
        throw new ChatException("Chat not found with id "+chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        Optional<Chat> optional = chatRepository.findById(chatId);

        if(optional.isPresent()){
            Chat chat = optional.get();
            if(chat.getUsers().contains(reqUser)){
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are not member of this Group");
        }
        throw new ChatException("Chat not found with id "+chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> optional = chatRepository.findById(chatId);

        User user = userService.findUserById(userId);

        if(optional.isPresent()){
            Chat chat = optional.get();
            if(chat.getAdmin().contains(reqUser)){    //to get removed by admin
                chat.getUsers().remove(user);
                return chatRepository.save(chat);

            } else if (chat.getUsers().contains(reqUser)) {     //to leave the chat by self
                if(user.getId().equals(reqUser.getId())){
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }
                throw new UserException("Only admin can remove users");

        }
        throw new ChatException("Chat not found with id "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userid) throws ChatException, UserException {

        Optional<Chat> optional = chatRepository.findById(chatId);

        if(optional.isPresent()){
            Chat chat = optional.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
