package client.verify;

import chain.Transaction;
import org.apache.commons.lang3.SerializationUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignitureVerification {

    public static boolean checkSigniture(PublicKey publicKey, Transaction t, byte[] signiture) {
        try {
            Signature rsa = Signature.getInstance("SHA256withRSA");
            rsa.initVerify(publicKey);
            rsa.update(SerializationUtils.serialize(t));
            return rsa.verify(signiture);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] generateSignature(PrivateKey privateKey, Transaction dataHash) {
        try {
            Signature rsa = Signature.getInstance("SHA256withRSA");
            rsa.initSign(privateKey);
            rsa.update(SerializationUtils.serialize(dataHash));
            return rsa.sign();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
