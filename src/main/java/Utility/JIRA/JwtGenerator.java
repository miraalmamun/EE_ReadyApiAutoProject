package Utility.JIRA;

import java.net.URI;

public interface JwtGenerator {

    String generateJWT(String requestMethod, URI uri, int jwtExpiryWindowSeconds);
}
