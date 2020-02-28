package com.phenjuly.keycloak.spi.authenticator.provider;

import lombok.Getter;
import org.keycloak.credential.CredentialInput;

/**
 * @author PhenJuly
 * @create 2020/2/26
 * @since
 */
@Getter
public class PhoneCredentialInput implements CredentialInput {
    private final String credentialId;
    private final String type;
    private final PhoneCredentialModel.PhoneCredentialData credentialData;
    private final PhoneCredentialModel.PhoneSecretData secretData;

    public PhoneCredentialInput(String credentialId, String type, PhoneCredentialModel.PhoneCredentialData credentialData, PhoneCredentialModel.PhoneSecretData secretData) {
        this.credentialId = credentialId;
        this.type = type;
        this.credentialData = credentialData;
        this.secretData = secretData;
    }


    @Override
    public String getCredentialId() {
        return credentialId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getChallengeResponse() {
        throw new UnsupportedOperationException("phone  credential doesn't support getChallengeResponse");
    }
}
