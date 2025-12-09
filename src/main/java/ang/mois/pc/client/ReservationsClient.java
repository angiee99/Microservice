package ang.mois.pc.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ReservationsClient {

    private final RestTemplate restTemplate;

    @Value("${app.reservations.url}")
    private String reservationsBaseUrl;

    @Value("${app.reservations.url.reservations:/reservations}")
    private String reservationsPath;

    @Value("${app.reservations.url.computer:computerId}")
    private String computerIdParam;


    public ReservationsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * verifies if any reservation exists for the provided computer in the reservation microservice
     * @param computerId the id of a computer to verify reservations
     * @return true if any reservation exists, otherwise false
     */
    public boolean hasReservationsForPc(Long computerId) {
        // build the url
        String url = UriComponentsBuilder
                .fromUri(URI.create(reservationsBaseUrl))
                .path(reservationsPath)
                .queryParam(computerIdParam, computerId)
                .toUriString();

        ResponseEntity<Object[]> response =
                restTemplate.getForEntity(url, Object[].class);

        // verify if any reservation on the computer exists
        Object[] reservations = response.getBody();
        return reservations != null && reservations.length > 0;
    }
}