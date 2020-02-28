package com.phenjuly.keycloak.spi.user.storage.provider;

import com.phenjuly.keycloak.spi.user.storage.repository.UserRepository;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author PhenJuly
 */
@JBossLog
public class CustomizeUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputUpdater, CredentialInputValidator {

    private final KeycloakSession keycloakSession;
    private final ComponentModel componentModel;
    private final UserRepository userRepository;

    public CustomizeUserStorageProvider(KeycloakSession session, ComponentModel model, UserRepository repository) {
        this.keycloakSession = session;
        this.componentModel = model;
        this.userRepository = repository;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        log.debugv("isValid user credential:userId={0}, userName={1}", user.getId(), user.getUsername());
        log.debugv("isValid user input: {0},{1},{2}", input.getCredentialId(), input.getType(), input.getChallengeResponse());

        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }

        UserCredentialModel userCredentialModel = (UserCredentialModel) input;
        return userRepository.validateCredentials(user.getUsername(), userCredentialModel.getChallengeResponse());
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }

        UserCredentialModel userCredentialModel = (UserCredentialModel) input;
        return userRepository.updateCredentials(user.getUsername(), userCredentialModel.getChallengeResponse());
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        return Collections.emptySet();
    }

    @Override
    public void preRemove(RealmModel realm) {
        log.debugv("pre-remove realm");
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
        log.debugv("pre-remove group");
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
        log.debugv("pre-remove role");
    }

    @Override
    public void close() {
        log.debugv("close");
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        log.debugv("lookup user by id: realm={0} userId={1}", realm.getId(), id);

        String externalId = StorageId.externalId(id);
        return new UserAdapter(keycloakSession, realm, componentModel, userRepository.findUserById(externalId));
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        log.debugv("lookup user by username: realm={0} username={1}", realm.getId(), username);

        return new UserAdapter(keycloakSession, realm, componentModel, userRepository.findUserByUsernameOrEmail(username));
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        log.debugv("lookup user by username: realm={0} email={1}", realm.getId(), email);

        return getUserByUsername(email, realm);
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        return userRepository.getUsersCount();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        log.debugv("list users: realm={0}", realm.getId());

        return userRepository.getAllUsers().stream()
                .map(user -> new UserAdapter(keycloakSession, realm, componentModel, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        log.debugv("list users: realm={0} firstResult={1} maxResults={2}", realm.getId(), firstResult, maxResults);
        return getUsers(realm);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        log.debugv("search for users: realm={0} search={1}", realm.getId(), search);

        return userRepository.findUsers(search).stream()
                .map(user -> new UserAdapter(keycloakSession, realm, componentModel, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        log.debugv("search for users: realm={0} search={1} firstResult={2} maxResults={3}", realm.getId(), search, firstResult, maxResults);
        return searchForUser(search, realm);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        log.debugv("search for users with params: realm={0} params={1}", realm.getId(), params);
        List<UserModel> userModels = new ArrayList<>();
        return userModels;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        log.debugv("search for users with params: realm={0} params={1} firstResult={2} maxResults={3}", realm.getId(), params, firstResult, maxResults);
        List<UserModel> userModels = new ArrayList<>();

        return userModels;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        log.debugv("search for group members with params: realm={0} groupId={1} firstResult={2} maxResults={3}", realm.getId(), group.getId(), firstResult, maxResults);
        List<UserModel> userModels = new ArrayList<>();
        UserAdapter userAdapter = new UserAdapter(keycloakSession, realm, componentModel, userRepository.findUserById("6"));
        userModels.add(userAdapter);
        return userModels;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        log.debugv("search for group members: realm={0} groupId={1} firstResult={2} maxResults={3}", realm.getId(), group.getId());
        List<UserModel> userModels = new ArrayList<>();
        UserAdapter userAdapter = new UserAdapter(keycloakSession, realm, componentModel, userRepository.findUserById("6"));
        userModels.add(userAdapter);
        return userModels;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        log.debugv("search for group members: realm={0} attrName={1} attrValue={2}", realm.getId(), attrName, attrValue);
        List<UserModel> userModels = new ArrayList<>();
        return userModels;
    }
}
