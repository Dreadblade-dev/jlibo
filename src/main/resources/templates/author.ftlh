<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>
    <div class="mb-3">
        <h1 class="d-inline-block">
            ${author.getName()}
            <#if isAdmin>
                <a href="/author/${author.getId()}/edit" class="btn btn-primary">Edit</a>
            </#if>
        </h1>
        <#if author.getImageFilename()??>
            <img src="/images/author/${author.getImageFilename()}" class="rounded float-right col-md-4 mb-5"
                 style="height: 300px; width: auto;">
        </#if>
        <div class="col-md-8 d-inline-block">
            <p>${author.getDescription()}</p>
        </div>
    </div>
    <#if isAdmin>
        <#include "parts/bookUploadForm.ftlh">
    </#if>
    <h3 class="mt-5">
        <#if books?size gt 0>
            ${books?size} book<#if books?size gt 1 && books?size != 0>s</#if>:
        </#if>
    </h3>
    <#include "parts/bookList.ftlh">
</@c.page>