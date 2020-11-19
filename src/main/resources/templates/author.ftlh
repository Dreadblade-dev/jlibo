<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>
    <div>
        <h1>
            ${author.getName()}
            <#if isAdmin>
                <a href="/author/${author.getId()}/edit" class="btn btn-primary">Edit</a>
            </#if>
        </h1>
    </div>

    <p>${author.getDescription()}</p>
    <#if isAdmin>
        <#include "parts/bookUploadForm.ftlh">
    </#if>
    <h3>
        ${books?size} book<#if books?size gt 1 || books?size == 0>s</#if>:
    </h3>
    <#include "parts/bookList.ftlh">
</@c.page>