package ang.mois.pc.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Component
public class ReservationsClient {

    private final RestTemplate restTemplate;

    @Value("${app.gateway.url}")
    private String gatewayBaseUrl;

    public ReservationsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean hasReservationsForPc(Long computerId) {
        String url = gatewayBaseUrl + "/reservations?computerId=" + computerId;

        ResponseEntity<Object[]> response =
                restTemplate.getForEntity(url, Object[].class);

        Object[] reservations = response.getBody();
        return reservations != null && reservations.length > 0;
    }
}