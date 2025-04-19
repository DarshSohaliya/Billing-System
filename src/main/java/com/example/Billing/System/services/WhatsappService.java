package com.example.Billing.System.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    @Value("${twilio.account.sid}")
   private String accountsid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String whatsappNumber;

    @Value("${twilio.sms.number}")
    private String smsNumber;

    @PostConstruct
    public void init(){
        Twilio.init(accountsid,authToken);
    }
    public void sendWhatsApp(String toPhoneNumber,String messageText){
        System.out.println("SIIDH ::::::" + accountsid );
        System.out.println("authTOKEN ::::::" + authToken );
        System.out.println("numbegfe ::::::" + whatsappNumber);
       System.out.println("ToNUMBER ::: "+toPhoneNumber);
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:"+toPhoneNumber),
                    new PhoneNumber(whatsappNumber),
                    messageText
            ).create();
            System.out.println("WhatsApp message sent. SID: " + message.getSid());
        } catch (Exception e) {
            System.out.println("Failed to send WhatsApp message: " + e.getMessage());
        }
    }
    public void sendSMS(String toNumber,String messageText){
        try{
            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(smsNumber),
                    messageText
                    ).create();
            System.out.println("SMS send: "+ message.getSid());
        } catch (Exception e) {
            System.out.println("Failed to send SMS :" + e.getMessage());
        }
    }
    }


