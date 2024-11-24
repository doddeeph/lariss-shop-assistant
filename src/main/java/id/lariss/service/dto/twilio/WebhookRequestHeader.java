package id.lariss.service.dto.twilio;

import java.util.Map;
import java.util.Objects;

public class WebhookRequestHeader {

    private String twilioSignature;

    public WebhookRequestHeader(Map<String, String> requestHeader) {
        this.twilioSignature = requestHeader.getOrDefault("X-Twilio-Signature", "");
    }

    public String getTwilioSignature() {
        return twilioSignature;
    }

    public void setTwilioSignature(String twilioSignature) {
        this.twilioSignature = twilioSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WebhookRequestHeader that = (WebhookRequestHeader) o;
        return Objects.equals(twilioSignature, that.twilioSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(twilioSignature);
    }

    @Override
    public String toString() {
        return "WebhookRequestHeader{" + "twilioSignature='" + twilioSignature + '\'' + '}';
    }
}
