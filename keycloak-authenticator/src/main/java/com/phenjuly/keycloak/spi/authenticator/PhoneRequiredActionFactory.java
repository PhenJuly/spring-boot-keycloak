package com.phenjuly.keycloak.spi.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * @author PhenJuly
 * @create 2020/2/27
 * @since
 */
public class PhoneRequiredActionFactory implements RequiredActionFactory {

    private static final PhoneRequiredActionProvider SINGLETON = new PhoneRequiredActionProvider();

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return SINGLETON;
    }


    @Override
    public String getId() {
        return PhoneRequiredActionProvider.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "Secret Phone Code";
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

}