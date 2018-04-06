package io.clhost.devopstask.mail;

public class Mail {
    private final String subject;
    private final String text;

    public Mail(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "mail: [" +
                "subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ']';
    }
}
