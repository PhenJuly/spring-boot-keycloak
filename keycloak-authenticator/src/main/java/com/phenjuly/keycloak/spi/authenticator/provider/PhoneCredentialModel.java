package com.phenjuly.keycloak.spi.authenticator.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialModel;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;

/**
 * 数据库中数据存储认证数据模型
 *
 * @author PhenJuly
 * @create 2020/2/26
 * @since
 */

public class PhoneCredentialModel extends CredentialModel {
    /**
     * 认证类型
     */
    public static final String CREDENTIAL_TYPE = "PHONE_VERIFICATION_CODE";

    private final PhoneCredentialData phoneCredentialData;
    private final PhoneSecretData phoneSecretData;

    private PhoneCredentialModel(PhoneCredentialData phoneCredentialData, PhoneSecretData phoneSecretData) {
        this.phoneCredentialData = phoneCredentialData;
        this.phoneSecretData = phoneSecretData;
    }

    private PhoneCredentialModel(String question, String answer) {
        this.phoneCredentialData = new PhoneCredentialData(question);
        this.phoneSecretData = new PhoneSecretData(answer);
    }

    public static PhoneCredentialModel create(String phone, String code) {
        PhoneCredentialModel credentialModel = new PhoneCredentialModel(phone, code);
        credentialModel.fillCredentialModelFields();
        return credentialModel;
    }

    public static PhoneCredentialModel create(CredentialModel credentialModel) {
        try {
            PhoneCredentialData credentialData = JsonSerialization.readValue(credentialModel.getCredentialData(), PhoneCredentialData.class);
            PhoneSecretData secretData = JsonSerialization.readValue(credentialModel.getSecretData(), PhoneSecretData.class);

            PhoneCredentialModel phoneCredentialModel = new PhoneCredentialModel(credentialData, secretData);
            phoneCredentialModel.setUserLabel(credentialModel.getUserLabel());
            phoneCredentialModel.setCreatedDate(credentialModel.getCreatedDate());
            phoneCredentialModel.setType(CREDENTIAL_TYPE);
            phoneCredentialModel.setId(credentialModel.getId());
            phoneCredentialModel.setSecretData(credentialModel.getSecretData());
            phoneCredentialModel.setCredentialData(credentialModel.getCredentialData());
            return phoneCredentialModel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillCredentialModelFields() {
        try {
            setCredentialData(JsonSerialization.writeValueAsString(phoneCredentialData));
            setSecretData(JsonSerialization.writeValueAsString(phoneSecretData));
            setType(CREDENTIAL_TYPE);
            setCreatedDate(Time.currentTimeMillis());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PhoneCredentialData getPhoneCredentialData() {
        return phoneCredentialData;
    }

    public PhoneSecretData getPhoneSecretData() {
        return phoneSecretData;
    }

    @Getter
    public static class PhoneCredentialData {
        private final String phone;

        @JsonCreator
        public PhoneCredentialData(@JsonProperty("phone") String phone) {
            this.phone = phone;
        }

    }

    @Getter
    public static class PhoneSecretData {
        private final String verificationCode;

        @JsonCreator
        public PhoneSecretData(@JsonProperty("verificationCode") String verificationCode) {
            this.verificationCode = verificationCode;
        }


    }

}
