package natlex.example.geologicalproject.aspect;

import jakarta.servlet.http.HttpServletRequest;
import natlex.example.geologicalproject.exceptions.NotAuthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {
    @Autowired
    private HttpServletRequest request;

    @Value("${auth.header.name}")
    String header;
    @Value("${competition.management.token}")
    private String competitionManagementToken;

    @Around("@annotation(natlex.example.geologicalproject.aspect.annotation.Authorized)")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = request.getHeader(header);
        if (token == null || !token.equals(competitionManagementToken)) {
            throw new NotAuthorizedException("Not Authorized");
        }
        return joinPoint.proceed();
    }
}
