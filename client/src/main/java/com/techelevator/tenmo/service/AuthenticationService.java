package com.techelevator.tenmo.service;

import com.techelevator.tenmo.Dto.CredentialsDto;
import com.techelevator.tenmo.Dto.TokenDto;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AuthenticationService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public String login(String username, String password) {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setUsername(username);
        credentialsDto.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> entity = new HttpEntity<>(credentialsDto, headers);
        String token = null;
        try {
            // Add code here to send the request to the API and get the token from the response.
            ResponseEntity<TokenDto> response = restTemplate.exchange(API_BASE_URL + "login", HttpMethod.POST, entity, TokenDto.class);
            TokenDto body = response.getBody();
            if (body!=null) {
                token = body.getToken();
                //This is Step 2 (above)
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());;
        }
        return token;
    }
}
