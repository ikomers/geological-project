/*
package natlex.example.geologicalproject.service.impl;


import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.exceptions.NotAuthorizedException;
import natlex.example.geologicalproject.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {
    @Value("${auth.token}")
    private String validToken;

    @Override
    public void validateToken(String providedToken) throws NotAuthorizedException {
        if (!validToken.equals(providedToken)) {
            throw new NotAuthorizedException("Not Authorized");
        }
    }
}*/
