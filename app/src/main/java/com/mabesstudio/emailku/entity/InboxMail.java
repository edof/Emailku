package com.mabesstudio.emailku.entity;

import java.io.Serializable;

/**
 * Created by Misbahul on 19/07/2017.
 */

public class InboxMail implements Serializable {
    public int id_inbox; //no usage
    public String sender, subject, snippet, timestamp, image, star, attachment;
}
