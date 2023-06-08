package com.shire42.api.contoller.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {

    @NotBlank(message = "Body is mandatory")
    private String body;

    @NotBlank(message = "Topic is mandatory")
    private String topic;

    @NotBlank(message = "Target is mandatory")
    @Email(message = "Target email is invalid")
    private String target;

    @NotBlank(message = "From is mandatory")
    @Email(message = "Email email is invalid")
    private String from;

    private String fileName = "default.pdf";

    private String filePassword = "testtest";

}
