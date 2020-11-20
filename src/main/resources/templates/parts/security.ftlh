<#assign
    context = Session.SPRING_SECURITY_CONTEXT??
>

<#if context>
    <#assign
        currentUser = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        username = currentUser.getUsername()
        currentUserId = currentUser.getId()
        isAdmin = currentUser.isAdmin()
    >
<#else>
    <#assign
        username = "guest"
        currentUserId = -1
        isAdmin = false
    >
</#if>