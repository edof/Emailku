package com.mabesstudio.emailku.entity;

import java.io.Serializable;

public class InboxMail implements Serializable {
    public int id_inbox; //no usage
    public String sender, subject, snippet, timestamp, image, star, attachment;
}
