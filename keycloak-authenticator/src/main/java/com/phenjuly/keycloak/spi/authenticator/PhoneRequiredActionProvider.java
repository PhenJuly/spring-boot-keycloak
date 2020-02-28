package com.phenjuly.keycloak.spi.authenticator;

import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialModel;
import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialProvider;
import org.keycloak.authentication.CredentialRegistrator;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.credential.CredentialProvider;

import javax.ws.rs.core.Response;

/**
 * @author PhenJuly
 * @create 2020/2/27
 * @since
 */
public class PhoneRequiredActionProvider implements RequiredActionProvider, CredentialRegistrator {
    public static final String PROVIDER_ID = "secret_phone_config";


    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("secret-phone-config.ftl");
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        String phone = (context.getHttpRequest().getDecodedFormParameters()
                .getFirst("secret_phone"));
        String phoneCode = (context.getHttpRequest().getDecodedFormParameters()
                .getFirst("secret_phone_code"));
        PhoneCredentialProvider phoneCredentialProvider = (PhoneCredentialProvider) context.getSession()
                .getProvider(CredentialProvider.class, "secret-phone");
        phoneCredentialProvider.createCredential(context.getRealm(), context.getUser(), PhoneCredentialModel.create(phone, phoneCode));
        context.success();
    }

    @Override
    public void close() {

    }

    @Override
    public void evaluateTriggers(RequiredActionContext context) {

    }
}
