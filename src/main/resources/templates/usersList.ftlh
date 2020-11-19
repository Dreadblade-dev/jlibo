<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <table class="table table-hover">
        <thead>
            <th scope="col">Id</th>
            <th scope="col">Username</th>
            <th scope="col">Roles</th>
            <th scope="col">Active</th>
        </thead>
    <#list users as user>
        <tr <#if user.getId() == currentUserId>class="table-primary"</#if>>
            <td>${user.getId()}</td>
            <td>${user.getUsername()}</td>
            <td>
                <#list user.getRoles() as role>
                    ${role.name()}&nbsp
                </#list>
            </td>
            <td>${user.isActive()?string('yes', 'no')}</td>
        </tr>
    </#list>
    </table>
</@c.page>