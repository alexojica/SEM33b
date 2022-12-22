package rowing.notification.domain.notification.strategy;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import rowing.notification.domain.notification.Notification;

@Data
@Component
public class KafkaStrategy implements Strategy {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topicName}")
    String topicName;

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.KAFKA;
    }

    @Override
    public void notifyUser(Notification notification) {
        String message = notification.retrieveSubject() + "\n" + notification.retrieveBody();
        JSONObject json = new JSONObject();
        json.put(notification.getUsername(), message);
        kafkaTemplate.send(topicName, json.toString());
        System.out.println(message);
    }
}
