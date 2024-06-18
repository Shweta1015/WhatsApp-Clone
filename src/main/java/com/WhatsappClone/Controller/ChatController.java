package com.WhatsappClone.Controller;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Chat;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Request.SingleChatRequest;
import com.WhatsappClone.Response.ApiResponse;
import com.WhatsappClone.Services.ChatService;
import com.WhatsappClone.Services.GroupChatRequest;
import com.WhatsappClone.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private ChatService chatService;
    private UserService userService;

    public ChatController(ChatService chatService, UserService userService){
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization")String jwt) throws UserException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest, @RequestHeader("Authorization")String jwt) throws UserException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.creatGroup(groupChatRequest, reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {

        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(@RequestHeader("Authorization")String jwt) throws UserException {

        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(userId, chatId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {

        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId, reqUser.getId());
        ApiResponse response = new ApiResponse("Chat is deleted Successfully", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
