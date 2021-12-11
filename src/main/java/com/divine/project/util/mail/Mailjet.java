package com.divine.project.util.mail;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Mailjet {

    public int sendMail(String to, String toName, String subject, String body) throws MailjetSocketTimeoutException, MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient("6d41470778ec09b5b0fbe15a08a5957e", "d032896b44953bdb32dcd66fdadd3194", new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "vinewears@divineworld.club")
                                        .put("Name", "Vine Wears Online store"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)
                                                .put("Name", toName)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, body)));
//                                .put(Emailv31.Message.HTMLPART, "<h3>Dear Oga Solo 1, welcome to </h3><br />May the peace of the Lord be with you!")));
//                                .put(Emailv31.Message.CUSTOMID, "vinewears")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
        return response.getStatus();
    }

}
