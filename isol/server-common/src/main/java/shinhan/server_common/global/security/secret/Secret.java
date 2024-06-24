package shinhan.server_common.global.security.secret;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Secret {

    private static final String JWT_SECRET_KEY = System.getenv("JWT_SECRET_KEY");

    public static SecretKey getJwtKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}