<#import "parts/common.ftlh" as c>
<@c.page>
    <h2>${author.getName()}</h2>
    <h3>
        ${books?size} book<#if books?size gt 1>s</#if>:
    </h3>
    <#include "parts/bookList.ftlh">
</@c.page>