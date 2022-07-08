package top.twip.common.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-07 17:22
 */
@Component
public class TokenHandler {
    static final private String SALT = "keysafe";

    /**
     * 获取token
     * @param id 用户id
     * @param card 用户账号
     * @param pass 用户密码
     * @return String token
     */
    public String getToken(String id,String card,String pass){
        String totalString = id + "|" + card + "|" + pass;
        return plaintextToCiphertext(totalString);
    }


    /**
     * 验证token是否合法
     * @param token token
     * @param id 用户id
     * @param card 用户账号
     * @param pass 用户密码
     * @return Boolean Boolean
     */
    public Boolean checkToken(String token, String id, String card, String pass){
        String totalString = id + "|" + card + "|" + pass;
        return ciphertextToPlaintext(totalString, token);
    }

    // 明文转密文
    private String plaintextToCiphertext(String plaintext){
        return BCrypt.hashpw(plaintext,SALT);
    }

    // 明文与密文做对比
    private Boolean ciphertextToPlaintext(String plaintext, String ciphertext){
        return BCrypt.checkpw(plaintext,ciphertext);
    }
}
