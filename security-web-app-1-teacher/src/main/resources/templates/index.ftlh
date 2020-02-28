<#import "/spring.ftl" as spring>
<#assign base=springMacroRequestContext.getContextUrl("")>

<html>

<head>
    <title>My awesome landing page</title>
</head>

<body>

<h1>Landing page</h1>
<p>
    <a href="${base}/teachers">ALL Teachers</a>
</p>

<p>
    <a href="${base}/teachers/courses">Teachers All Courses</a>
</p>

<p>
    <a href="${base}/logout">Logout</a>
</p>

</body>

</html>