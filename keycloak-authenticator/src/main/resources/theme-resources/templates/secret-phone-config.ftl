<#import "select.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
        ${msg("loginTitle",realm.name)}
    <#elseif section = "header">
        Setup Secret Phone
    <#elseif section = "form">
        <form id="kc-totp-login-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="totp" class="${properties.kcLabelClass!}">请输入您的手机号码:</label>
                </div>

                <div class="${properties.kcInputWrapperClass!}">
                    <input id="totp" name="secret_phone" type="text" class="${properties.kcInputClass!}"/>
                </div>

                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="totp" class="${properties.kcLabelClass!}">请输入您的短信验证码：</label>
                </div>

                <div class="${properties.kcInputWrapperClass!}">
                    <input id="totp" name="secret_phone_code" type="text" class="${properties.kcInputClass!}"/>
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}"
                               name="login" id="kc-login" type="submit" value="${msg("doSubmit")}"/>
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>