package com.shire42.api.contoller.rest;

import com.shire42.api.model.EmailMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EmailResponse {

    private Date sent;

    private EmailRequest message;

}
