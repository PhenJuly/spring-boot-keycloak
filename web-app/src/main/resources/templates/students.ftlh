<#import "/spring.ftl" as spring>
<html>
<h1>My products</h1>
<ul>
    <#list results as product>
        <li>${product}</li>
    </#list>
</ul>
<p> <a href="/web/logout">Logout</a> </p>

</html>