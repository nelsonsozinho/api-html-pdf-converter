package com.shire42.api.model;

import com.shire42.api.compose.PdfCompose;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailMessage {

    private String body;
    private String topic;
    private String target;
    private String from;
    private PdfCompose attachment;

}
