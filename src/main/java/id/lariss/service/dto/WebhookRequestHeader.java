package id.lariss.service.dto;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@NoArgsConstructor
public class WebhookRequestHeader {

    private String twilioSignature;

    public WebhookRequestHeader(Map<String, String> requestHeader) {
        this.twilioSignature = requestHeader.getOrDefault("X-Twilio-Signature", "");
    }

    @Override
    public String toString() {
        return "WebhookRequestHeader{" + "twilioSignature='" + twilioSignature + '\'' + '}';
    }
}
