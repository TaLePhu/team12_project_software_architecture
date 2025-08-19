package iuh.se.team.webbookstore_backend.services;

public interface EmailService {
    public void sendEmail(String from, String to, String subject, String text, boolean isHtml);
}
