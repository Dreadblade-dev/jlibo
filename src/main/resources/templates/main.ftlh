<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<#include "parts/security.ftlh">
<@c.page>
    <#if message??>
        <p>${message}</p>
    </#if>

    <#include "parts/bookUploadForm.ftlh">
    <#include "parts/bookList.ftlh">
</@c.page>