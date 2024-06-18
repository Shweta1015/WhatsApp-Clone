package com.WhatsappClone.Controller;

import com.WhatsappClone.Exception.ChatException;
import com.WhatsappClone.Exception.MessageException;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.Message;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Request.SendMessageRequest;
import com.WhatsappClone.Response.ApiResponse;
import com.WhatsappClone.Services.MessageService;
import com.WhatsappClone.Services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private UserService userService;

    public MessageController(MessageService messageService, UserService userService){
        this.messageService = messageService;
        this.userService = userService;
    }
    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest request, @RequestHeader("Authorization")String jwt) throws ChatException, UserException {

        User user = userService.findUserProfile(jwt);
        request.setUserId(user.getId());
        Message message = messageService.sendMessage(request);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessageHandler(@PathVariable Integer chatId, @RequestHeader("Authorization")String jwt) throws ChatException, UserException {

        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessages(chatId, user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId, @RequestHeader("Authorization")String jwt) throws ChatException, UserException, MessageException {

        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse response = new ApiResponse("message deleted successfully", false);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
