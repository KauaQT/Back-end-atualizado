package sptech.school.Chat.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sptech.school.Chat.Service.ChatService;
import sptech.school.dto.MessageDto;

@RestController
@RequestMapping("/notification")
public class ChatController {

    @Autowired
    private ChatService webSocketService;

    @PostMapping
    @CrossOrigin
    public void sendMessage(@RequestBody MessageDto messageDto){
        System.out.println("Sending message: " + messageDto);
        webSocketService.sendMessage(messageDto);
        System.out.println("Message sent: " + messageDto);
    }

    @PostMapping("/exchange")
    @CrossOrigin
    public void exchangeMessages(@RequestBody MessageDto messageDto1, @RequestBody MessageDto messageDto2){
        webSocketService.sendMessage(messageDto1);
        webSocketService.sendMessage(messageDto2);
    }
}
