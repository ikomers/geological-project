package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.exceptions.NotAuthorizedException;

public interface TokenValidationService {
    void validateToken(String providedToken) throws NotAuthorizedException;
}