package client.verify;

import java.security.*;

public class KeyGenerator {

    public static KeyPair generateKeys() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048, new SecureRandom());

            KeyPair keyPair = keygen.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            printString(privateKey.toString(), "private key");
            PublicKey publicKey = keyPair.getPublic();
            printString(publicKey.toString(), "public key");
            return keyPair;
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void printString(String data, String generatedString) {
        StringBuilder sb = new StringBuilder();
        sb.append(KeyGenerator.class.toString());
        sb.append(">>> Generating ");
        sb.append(generatedString);
        sb.append(": ");
        sb.append(data);
        System.out.println(sb.toString());
    }
}
