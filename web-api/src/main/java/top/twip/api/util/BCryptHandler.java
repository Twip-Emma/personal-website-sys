package top.twip.api.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * @Author: 七画一只妖
 * @Date: 2022-06-22 11:04
 */
@Component
public class BCryptHandler {
    static final private String SALT = BCrypt.gensalt(10);

    // 明文转密文
    public String plaintextToCiphertext(String plaintext){
        return BCrypt.hashpw(plaintext, SALT);
    }

    // 明文与密文做对比
    public Boolean ciphertextToPlaintext(String plaintext, String ciphertext){
        return BCrypt.checkpw(plaintext,ciphertext);
    }
}
