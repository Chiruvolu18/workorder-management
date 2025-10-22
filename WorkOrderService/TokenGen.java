import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class TokenGen {
    public static void main(String[] args) throws Exception {
        String secret = "MySecretKeyForJWTTokenGenerationAndValidation12345678901234567890";
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        long iat = Instant.now().getEpochSecond();
        long exp = iat + 3600;
        String payload = "{\"sub\":\"testuser\",\"roles\":[\"FIELD_CREW\"],\"iat\":" + iat + ",\"exp\":" + exp + "}";

        String headerB = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
        String payloadB = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));
        String signingInput = headerB + "." + payloadB;
        String sig = base64UrlEncode(hmacSha256(signingInput, secret));

        System.out.println(signingInput + "." + sig);
    }

    private static byte[] hmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }
}
