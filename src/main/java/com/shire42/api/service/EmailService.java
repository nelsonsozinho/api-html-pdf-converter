package com.shire42.api.service;

import com.shire42.api.compose.MessageAttachmentCompose;
import com.shire42.api.compose.PdfCompose;
import com.shire42.api.contoller.rest.EmailRequest;
import com.shire42.api.contoller.rest.EmailResponse;
import com.shire42.api.model.EmailMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class EmailService {

    private MessageAttachmentCompose compose;

    public EmailResponse sendEmail(final EmailRequest emailRequest) {
        compose.sendEmail(createEmailMessage(emailRequest));
        return new EmailResponse(new Date(), emailRequest);
    }

    private EmailMessage createEmailMessage(EmailRequest request) {
        return EmailMessage.builder()
                .body(request.getBody())
                .topic(request.getTopic())
                .target(request.getTarget())
                .from(request.getFrom())
                .attachment(new PdfCompose(request.getBody(), request.getFileName(), request.getFilePassword()))
                .build();
    }

}
