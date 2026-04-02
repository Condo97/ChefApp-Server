package com.pantrypro.apple.apns;

import appletransactionclient.JWTSigner;
import com.pantrypro.util.PersistentLogger;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class APNSJWTGenerator {

    public static String generateJWT(JWTSigner signer, String issuerID) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        long correctedTime = ((long)(System.currentTimeMillis() / 1_800_000)) * 1_800_000;
        PersistentLogger.info(PersistentLogger.APPLE, "APNS JWT corrected time: " + correctedTime);

        return signer.signJWT(Map.of(
                "iss", issuerID,
                "iat", correctedTime / 1000l
        ));
    }

}
