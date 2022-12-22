package rowing.notification.config;

import lombok.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.sasl.jaas-config}")
    private String kafkaJaasConfig;

    @Value("${spring.kafka.properties.sasl-mechanism}")
    private String kafkaSaslMechanism;

    @Value("${spring.kafka.properties.security-protocol}")
    private String kafkaSecurityProtocol;

    @Value("${useAuthentication}")
    private boolean useAuthentication;

    /**
     * Configures the kafka producer with values from application.properties

     * @return properties of the kafka producer
     */
    public Map<String, Object> producerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        if (useAuthentication) {
            props.put("sasl.mechanism", kafkaSaslMechanism);
            props.put("sasl.jaas.config", kafkaJaasConfig);
            props.put("security.protocol", kafkaSecurityProtocol);
        }
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
