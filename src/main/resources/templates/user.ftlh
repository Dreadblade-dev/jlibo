<#import "parts/common.ftlh" as c>
<#import "parts/bookFilter.ftlh" as f>
<#include "parts/security.ftlh">

<@c.page>
    <div>
        <h1>
            ${user.getUsername()}
            <#if user.getId() == currentUserId || isAdmin>
                <a href="/user/${user.getId()}/edit" class="btn btn-primary">Edit</a>
            </#if>
        </h1>
        <#if user.getImageFilename()??>
            <img src="/images/user/${user.getImageFilename()}" class="rounded float-right mb-5"
                 style="height: 200px; width: auto;">
        </#if>
            <#if user.isActive()>
                <p>
                    Active
                </p>
            </#if>
        <h5>${user.getUsername()}'s roles:</h5>
        <ul>
            <#list user.getRoles() as role>
                <li>${role.name()}</li>
            </#list>
        </ul>
        <h5>${user.getUsername()}'s uploaded books:</h5>
        <@f.filterForm "/user/${user.getId()}"/>
        <#include "parts/bookList.ftlh">
    </div>
</@c.page>