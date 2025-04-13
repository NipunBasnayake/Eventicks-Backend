package edu.icet.eventicks.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public ResponseMessage processMessage(Message message) throws Exception {
//        Thread.sleep(500);
//        String sanitizedContent = HtmlUtils.htmlEscape(message.getContent());
//        return new ResponseMessage(message.getSender() + " says: " + sanitizedContent);
//    }
}
