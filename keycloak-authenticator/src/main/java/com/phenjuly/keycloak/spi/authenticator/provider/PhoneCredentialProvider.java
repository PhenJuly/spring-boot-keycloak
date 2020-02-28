package com.phenjuly.keycloak.spi.authenticator.provider;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.common.util.Time;
import org.keycloak.credential.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

/**
 * 扩展CredentialModel成自定义的PhoneCredentialModel
 *
 * @author PhenJuly
 */
@JBossLog
public class PhoneCredentialProvider implements CredentialProvider<PhoneCredentialModel>, CredentialInputValidator {

    protected KeycloakSession session;

    public PhoneCredentialProvider(KeycloakSession session) {
        this.session = session;
    }

    private UserCredentialStore getCredentialStore() {
        return session.userCredentialManager();
    }

    @Override
    public String getType() {
        return PhoneCredentialModel.CREDENTIAL_TYPE;
    }

    @Override
    public PhoneCredentialModel getCredentialFromModel(CredentialModel model) {
        return PhoneCredentialModel.create(model);
    }

    @Override
    public CredentialModel createCredential(RealmModel realm, UserModel user, PhoneCredentialModel credentialModel) {
        if (credentialModel.getCreatedDate() == null) {
            credentialModel.setCreatedDate(Time.currentTimeMillis());
        }
        return getCredentialStore().createCredential(realm, user, credentialModel);
    }

    @Override
    public void deleteCredential(RealmModel realm, UserModel user, String credentialId) {
        getCredentialStore().removeStoredCredential(realm, user, credentialId);
    }

    /**
     * 凭据对给定领域中的给定用户是否有效
     *
     * @param realm
     * @param user
     * @param input
     * @return
     */
    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!(input instanceof PhoneCredentialInput)) {
            log.debug("Expected instance of UserCredentialModel for CredentialInput");
            return false;
        }

        PhoneCredentialInput phoneCredentialInput = (PhoneCredentialInput) input;
        if (!input.getType().equals(getType())) {
            return false;
        }
        PhoneCredentialModel.PhoneCredentialData credentialData = phoneCredentialInput.getCredentialData();
        if (credentialData == null) {
            return false;
        }
        PhoneCredentialModel.PhoneSecretData secretData = phoneCredentialInput.getSecretData();
        if (secretData == null) {
            return false;
        }

        CredentialModel credentialModel = getCredentialStore().getStoredCredentialById(realm, user, phoneCredentialInput.getCredentialId());

        PhoneCredentialModel credentialFromModel = getCredentialFromModel(credentialModel);

        // TODO PhoneCredentialModel 从写equals hasCodes
        return credentialFromModel.getPhoneSecretData().getVerificationCode().equals(secretData.getVerificationCode())
                && credentialFromModel.getPhoneCredentialData().getPhone().endsWith(credentialData.getPhone());
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return getType().equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) {
            return false;
        }
        return !getCredentialStore().getStoredCredentialsByType(realm, user, credentialType).isEmpty();
    }

    @Override
    public void close() {

    }


}
