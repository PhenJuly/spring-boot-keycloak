<#import "/spring.ftl" as spring>
<#assign base=springMacroRequestContext.getContextUrl("")>

<html>
<h1>My products</h1>
<ul>
    <li>${result}</li>
</ul>
<p><a href="${base}/logout">Logout</a></p>

</html>