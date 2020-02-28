<#import "/spring.ftl" as spring>
<#assign base=springMacroRequestContext.getContextUrl("")>

<html>
<h1>My hero college</h1>
student courses:
<ul>
    <#list results as course>
        <a href="${base}/students/courses${course}">
            <li>${course}</li>
        </a>
    </#list>
</ul>
<p>
    <a href="${base}/logout">Logout</a>
</p>

</html>