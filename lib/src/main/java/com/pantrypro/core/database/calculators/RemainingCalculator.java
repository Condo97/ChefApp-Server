package com.pantrypro.core.database.calculators;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.pantrypro.common.IntegerFromBoolean;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
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

    public abstract Long calculateRemaining(String authToken) throws DBSerializerException, SQLException, InterruptedException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException;

}
