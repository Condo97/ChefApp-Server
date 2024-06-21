package com.pantrypro.database.calculators;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public abstract class RemainingCalculator {

    protected abstract Integer getCapFromPremium(boolean isPremium);

    public abstract Long calculateRemaining(String authToken) throws DBSerializerException, SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, AppStoreErrorResponseException;

}
