package com.WhatsappClone.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRequest {
    private List<Integer> userIds;
    private String chatName;
    private String ChatImage;


}
