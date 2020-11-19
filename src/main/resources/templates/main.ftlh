<#import "parts/common.ftlh" as c>
<@c.page>
    <#if message??>
        <p>${message}</p>
    </#if>

    <#include "parts/bookList.ftlh">
</@c.page>