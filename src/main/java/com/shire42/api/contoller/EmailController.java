package com.shire42.api.contoller;

import com.shire42.api.contoller.rest.EmailRequest;
import com.shire42.api.contoller.rest.EmailResponse;
import com.shire42.api.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email")
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody final EmailRequest emailMessage) {
        return ResponseEntity.ok(emailService.sendEmail(emailMessage));
    }

}
