<#import "/spring.ftl" as spring>
<#assign base=springMacroRequestContext.getContextUrl("")>

<html>
<h1>My hero college</h1>
<ul>
    teacher course:<li>${result}</li>
</ul>
<p><a href="${base}/logout">Logout</a></p>

</html>