<#import "/spring.ftl" as spring>
<#assign base=springMacroRequestContext.getContextUrl("")>

<html>
<h1>My hero college</h1>
<ul>
    <#list results as teacher>
        <a href="${base}/teachers/${teacher}">
            <li>${teacher}</li>
        </a>
    </#list>
</ul>
<p>
    <a href="/${base}/logout">Logout</a>
</p>

</html>