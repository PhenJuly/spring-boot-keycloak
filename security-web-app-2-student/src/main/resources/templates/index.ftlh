<#import "/spring.ftl" as spring>

<#assign base=springMacroRequestContext.getContextUrl("")>

<html>

<head>
    <title>My awesome landing page</title>
</head>

<body>

<h1>Landing page</h1>
<p>
    <a href="${base}/students">All Students</a>
</p>

<p>
    <a href="${base}/students/courses">Students All Courses</a>
</p>

<p>
    <a href="http://phenjuly.qicp.vip/security/web/t"> Teacher index</a>
</p>

<p>
    <a href="${base}/logout">Logout</a>
</p>

</body>

</html>