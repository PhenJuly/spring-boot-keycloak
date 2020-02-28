package com.phenjuly.keycloak.spi.user.storage.provider;

import com.phenjuly.keycloak.spi.user.storage.repository.UserRepository;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

/**
 * @author PhenJuly
 */
@JBossLog
public class CustomizeUserStorageProviderFactory implements UserStorageProviderFactory<CustomizeUserStorageProvider> {

    @Override
    public void init(Config.Scope config) {
        String someProperty = config.get("someProperty");
    }

    @Override
    public CustomizeUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new CustomizeUserStorageProvider(session, model, new UserRepository());
    }

    @Override
    public String getId() {
        return "phenjuly-keycloak-user-provider";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
                .property()
                .name("myParam")
                .label("My Param")
                .helpText("Some Description")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("some value")
                .add()
                .build();
    }
}
