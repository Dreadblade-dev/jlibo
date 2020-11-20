<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <h3>${user.getUsername()}</h3>
        <#if user.getImageFilename()??>
            <img src="/images/user/${user.getImageFilename()}" class="col-md-4">
        </#if>
    </div>
</@c.page>