package com.phenjuly.keycloak.spi.authenticator.provider;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;

/**
 * @author PhenJuly
 */
@JBossLog
public class PhoneCredentialProviderFactory implements CredentialProviderFactory<PhoneCredentialProvider> {

    public static final String PROVIDER_ID =  "phone_verification_code";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public CredentialProvider create(KeycloakSession session) {
        return new PhoneCredentialProvider(session);
    }
}
