<#assign
    context = Session.SPRING_SECURITY_CONTEXT??
>

<#if context>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        username = user.getUsername()
        currentUserId = user.getId()
        isAdmin = user.isAdmin()
    >
<#else>
    <#assign
        username = "guest"
        currentUserId = -1
        isAdmin = false
    >
</#if>