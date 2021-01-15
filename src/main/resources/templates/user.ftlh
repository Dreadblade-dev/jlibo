<#import "parts/common.ftlh" as c>
<#import "parts/bookList.ftlh" as l>
<#include "parts/security.ftlh">

<@c.page>
    <div>
        <h1>
            ${user.getHighestRoleName()} ${user.getUsername()}
            <#if user.getId() == currentUserId || isAdmin>
                <a href="/user/${user.getId()}/edit" class="btn btn-primary">Edit</a>
            </#if>
        </h1>
        <#if user.getImageFilename()??>
            <img src="/images/user/${user.getImageFilename()}" class="rounded float-right mb-5"
                 style="height: 200px; width: auto;">
        </#if>
        <h5>${user.getUsername()}'s uploaded books:</h5>
        <@l.bookList "/user/${user.getId()}"/>
    </div>
</@c.page>