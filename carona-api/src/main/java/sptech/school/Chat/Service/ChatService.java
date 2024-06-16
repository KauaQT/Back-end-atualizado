package sptech.school.Chat.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sptech.school.dto.MessageDto;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatService {

    private final SimpMessagingTemplate template;

    public void sendMessage(MessageDto message) {
        System.out.println("Sending message: " + message.getMessage());

        template.convertAndSend("/topic/message", message.getMessage());
        System.out.println("Message sent: " + message.getMessage());
    }
}
