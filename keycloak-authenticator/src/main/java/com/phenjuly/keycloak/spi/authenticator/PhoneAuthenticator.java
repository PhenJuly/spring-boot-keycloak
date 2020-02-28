package com.phenjuly.keycloak.spi.authenticator;

import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialInput;
import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialModel;
import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialProvider;
import com.phenjuly.keycloak.spi.authenticator.provider.PhoneCredentialProviderFactory;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.CredentialValidator;
import org.keycloak.common.util.ServerCookie;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * @author PhenJuly
 * @create 2020/2/26
 * @since
 */
public class PhoneAuthenticator implements CredentialValidator<PhoneCredentialProvider>, Authenticator {
    @Override
    public PhoneCredentialProvider getCredentialProvider(KeycloakSession session) {
        return session.getProvider(PhoneCredentialProvider.class, PhoneCredentialProviderFactory.PROVIDER_ID);
    }

    /**
     * 验证器是否与用户相关
     *
     * @return
     */
    @Override
    public boolean requiresUser() {
        return true;
    }

    /**
     * 是否为该Phone验证器配置了用户
     *
     * @param session
     * @param realm
     * @param user
     * @return
     */
    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return getCredentialProvider(session).isConfiguredFor(realm, user, getType(session));
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction("PHONE_VERIFICATION_CODE_CONFIG");
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
        Response challenge = context.form()
                .createForm("secret-phone.ftl");
        context.challenge(challenge);
    }

    protected boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("SECRET_QUESTION_ANSWERED");
        boolean result = cookie != null;
        if (result) {
            System.out.println("Bypassing secret question because cookie is set");
        }
        return result;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        boolean validated = validatePhoneCode(context);
        if (!validated) {
            Response challenge = context.form()
                    .setError("badSecret")
                    .createForm("secret-phone.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }
        setCookie(context);
        context.success();
    }

    protected boolean validatePhoneCode(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String phone = formData.getFirst("secret_phone");
        String phoneCode = formData.getFirst("secret_phone_code");
        String credentialId = formData.getFirst("credentialId");
        if (credentialId == null || credentialId.isEmpty()) {
            credentialId = getCredentialProvider(context.getSession())
                    .getDefaultCredential(context.getSession(), context.getRealm(), context.getUser()).getId();
        }


        PhoneCredentialModel.PhoneCredentialData phoneCredentialData = new PhoneCredentialModel.PhoneCredentialData(phone);
        PhoneCredentialModel.PhoneSecretData secretData = new PhoneCredentialModel.PhoneSecretData(phoneCode);

        PhoneCredentialInput model = new PhoneCredentialInput(credentialId, getType(context.getSession()), phoneCredentialData, secretData);
//        UserCredentialModel input = new UserCredentialModel(credentialId, getType(context.getSession()), phoneCode);
        return getCredentialProvider(context.getSession()).isValid(context.getRealm(), context.getUser(), model);
    }

    protected void setCookie(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        if (config != null) {
            maxCookieAge = Integer.valueOf(config.getConfig().get("cookie.max.age"));

        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
        addCookie(context, "PHONE_VERIFICATION_CODE", "true",
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    public void addCookie(AuthenticationFlowContext context, String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = context.getSession().getContext().getContextObject(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly, null);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }

    @Override
    public void close() {

    }
}
