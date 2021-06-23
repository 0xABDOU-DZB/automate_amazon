package ua.startit.support.mail;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class Email {

    private List<String> from;
    private List<String> to;
    private String subject;
    private String receivedDate;
    private String sentDate;
    private String contentType;
    private List<String> content;

    public List<String> getFrom() {
        return this.from;
    }

    public void setFrom(String[] from) {
        this.from = Lists.newArrayList();
        this.from.addAll(Arrays.asList(from));
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = Lists.newArrayList();
        this.to.addAll(Arrays.asList(to));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = Lists.newArrayList();
        this.content.add(content);
    }
}