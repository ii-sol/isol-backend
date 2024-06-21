package shinhan.server_common.global.security.secret;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Secret {

    private static final String JWT_SECRET_KEY = "b6ecbae5b1a8de21c5308f4eff3e9924b44b73c442e69e6530e83cab9da3c700";

    public static SecretKey getJwtKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}