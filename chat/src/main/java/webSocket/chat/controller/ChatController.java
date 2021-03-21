package webSocket.chat.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import webSocket.chat.domain.Chat;
import webSocket.chat.dto.ChatMessage;
import webSocket.chat.service.ChatService;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;

//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        if(ChatMessage.MessageType.JOIN.equals(message.getMessageType()))
//            message.setMessage(message.getSender() + "님이 입장하였습니다.");
//        messageTemplate.convertAndSend("/sub/chat/room/"+message.getRoomId());
//    }

    @MessageMapping("/{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessage test(@DestinationVariable Long roomId, ChatMessage message) {
        Chat chat = chatService.createChat(roomId, message.getSender(), message.getMessage());
        return ChatMessage.builder()
                .roomId(roomId)
                .sender(chat.getSender())
                .message(chat.getMessage())
                .build();
    }

}
