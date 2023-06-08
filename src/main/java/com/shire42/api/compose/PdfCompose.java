package com.shire42.api.compose;

import com.google.common.base.Strings;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfCompose {

    private String text;

    @Getter
    private Document document;

    @Getter
    private OutputStream stream;

    @Getter
    private String fileName;

    private String password;

    public PdfCompose(final String text, final String fileName) {
        this.fileName = fileName;
        prepareFile(text);
    }

    public PdfCompose(final String text, final String fileName, final String password) {
        this.password = password;
        this.fileName = fileName;
        prepareFile(text);
    }

    private void prepareFile(final String text) {
        this.text = text;
        this.document = new Document();
        this.stream = new ByteArrayOutputStream(text.length());

        try {
            generatePdfStream();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The process to encrypt the pdf streams with a password still not working. 
     */
    private void generatePdfStream() throws IOException, DocumentException {
        final ByteArrayOutputStream streamConverted = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(this.text, streamConverted);

        if(!Strings.isNullOrEmpty(this.password)) {
            PdfReader reader = new PdfReader(streamConverted.toByteArray());
            PdfStamper stamper = new PdfStamper(reader, streamConverted);

            stamper.setEncryption(this.password.getBytes(), this.password.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            reader.close();
        }

        InputStream is = new ByteArrayInputStream(streamConverted.toByteArray());

        this.stream = streamConverted;
    }

}
