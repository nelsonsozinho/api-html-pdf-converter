package com.shire42.api.compose;

import com.shire42.api.model.EmailMessage;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.RawMessage;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class MessageAttachmentCompose {

    public void sendEmail(final EmailMessage message) {
        final SesV2AsyncClient client = createSesClient();
        try {
            final SendEmailRequest request = createMimeMessage(message);
            final CompletableFuture<SendEmailResponse> response = client.sendEmail(request);
            response.get();
        } catch (IOException | MessagingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private SendEmailRequest createMimeMessage(final EmailMessage emailMessage) throws IOException, MessagingException {
        final Session session = Session.getDefaultInstance(new Properties());
        final MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setSubject(emailMessage.getTopic());
        mimeMessage.setFrom(emailMessage.getFrom());
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMessage.getTarget()));

        final MimeMultipart msgMixed = new MimeMultipart("mixed");
        final MimeBodyPart wrap = new MimeBodyPart();

        wrap.setContent(createBody(emailMessage.getBody()));
        msgMixed.addBodyPart(wrap);
        msgMixed.addBodyPart(createAttachment(emailMessage.getAttachment(), mimeMessage));
        mimeMessage.setContent(msgMixed);

        return makeObjectRequest(mimeMessage);
    }

    private MimeMultipart createBody(final String emailBody) throws MessagingException {
        final MimeBodyPart mimeBodyPart = new MimeBodyPart();
        final MimeMultipart msgBody = new MimeMultipart("alternative");

        mimeBodyPart.setContent(emailBody, "text/html; charset=UTF-8");
        msgBody.addBodyPart(mimeBodyPart);

        return msgBody;    }

    private MimeBodyPart createAttachment(final PdfCompose compose, final MimeMessage mimeMessage) throws MessagingException {
        final MimeBodyPart mimeBodyPart = new MimeBodyPart();
        final ByteArrayOutputStream st = (ByteArrayOutputStream) compose.getStream();
        final DataSource fds = new ByteArrayDataSource(st.toByteArray(), "application/pdf");

        mimeBodyPart.setDataHandler(new DataHandler(fds));
        mimeBodyPart.setFileName(compose.getFileName());

        return mimeBodyPart;
    }


    private SendEmailRequest makeObjectRequest(final MimeMessage mimeMessage) throws MessagingException, IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mimeMessage.writeTo(stream);

        final ByteBuffer buf = ByteBuffer.wrap(stream.toByteArray());
        final byte[] arr = new byte[buf.remaining()];
        buf.get(arr);

        final SdkBytes data = SdkBytes.fromByteArray(arr);
        final RawMessage rawMessage = RawMessage.builder()
                .data(data)
                .build();

        final EmailContent emailContent = EmailContent.builder()
                .raw(rawMessage)
                .build();

        return SendEmailRequest.builder()
                .content(emailContent)
                .build();
    }

    /**
     * Get the credentials in the file /home/user/.aws
     * @return SesV2AsyncClient
     */
    private SesV2AsyncClient createSesClient() {
        return SesV2AsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.builder().profileName("personal").build())
                .build();
    }

}
