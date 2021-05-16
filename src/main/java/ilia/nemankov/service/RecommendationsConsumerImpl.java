package ilia.nemankov.service;

import com.rabbitmq.jms.client.message.RMQBytesMessage;
import ilia.nemankov.dto.RecommendationMessageDTO;
import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Recommendation;
import ilia.nemankov.model.RecommendationId;
import ilia.nemankov.repository.ConfigurationRepository;
import ilia.nemankov.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


@Service
@AllArgsConstructor
public class RecommendationsConsumerImpl implements RecommendationsConsumer {

    private final ConfigurationRepository configurationRepository;
    private final RecommendationRepository recommendationRepository;

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Started to process new message");

            byte[] data = new byte[1024];
            ((RMQBytesMessage)message).readBytes(data);
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            RecommendationMessageDTO messageDTO = (RecommendationMessageDTO) is.readObject();

            int configurationId = messageDTO.getConfigurationId();

            Configuration configuration = configurationRepository.findById(configurationId).get();

            Recommendation recommendation = new Recommendation();
            recommendation.setId(new RecommendationId(messageDTO.getUserId(), configuration.getRooms().getHotel().getId()));
            recommendationRepository.save(recommendation);

            Thread.sleep(20 * 1000);

            System.out.println("Finished to process new message");
        } catch (IOException | ClassNotFoundException | JMSException | InterruptedException e) {
            System.out.println("Failed to process recommendation message");
            e.printStackTrace();
        }
    }

}
