package com.example.emailnotification.service;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import com.example.emailnotification.dto.UserDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailService //implements CommandLineRunner
{

    @Autowired
    private JavaMailSender emailSender;

    public void sendMail( UserDTO userDTO ) throws MessagingException
    {
         MimeMessage message = emailSender.createMimeMessage();
         MimeMessageHelper helper =
                new MimeMessageHelper(
                        message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name() );
         helper.setText( "<p>Hello" + userDTO.getFirstName()+" "+userDTO.getLastName() + "! \n"
                + "\n"
                + "This is an example of a mail template\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "The day I die, who will sing to Seville?</p>", true );

        helper.setTo( "aguridisig@gmail.com" );
        helper.setSubject( "Just a new notification!" );
        log.info( "Sending email...." );
        emailSender.send( message );

    }


    /*@Override
    public void run( final String... args ) throws Exception
    {
        this.sendMail( UserDTO.builder().firstName( "Paco" ).lastName( "El Pali" ).build() );
    }*/
}
