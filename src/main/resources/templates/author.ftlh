<#import "parts/common.ftlh" as c>
<#import "parts/bookFilter.ftlh" as f>
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
                 style="height: 300px; width: auto;"/>
        </#if>
        <div class="col-md-8 d-inline-block">
            <p>${author.getDescription()}</p>
        </div>
    </div>
    <#if isAdmin>
        <#if message??>
            <p>${message}</p>
        </#if>
        <#include "parts/bookUploadForm.ftlh">
    </#if>
    <h3 class="mt-5">
        <#if page.getContent()?size gt 0>
            ${page.getContent()?size} book<#if page.getContent()?size gt 1 && page.getContent()?size != 0>s</#if>:
        </#if>
    </h3>
    <@f.filterForm "/author/${author.getId()}"/>

    <#include "parts/bookList.ftlh">
</@c.page>