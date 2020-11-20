<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div>
        <form method="post" enctype="multipart/form-data" action="/user/${user.getId()}/edit" class="container">
            <#if isAdmin>
                <div class="form-group row">
                    <label for="staticId" class="col-sm-2 col-form-label">Id</label>
                    <div class="col-sm-10">
                        <input type="text" id="staticId" readonly value="${user.getId()}" class="form-control-plaintext" name="id">
                    </div>
                </div>
                <#else>
                <input type="hidden" value="${currentUserId}" name="id">
            </#if>
            <div class="form-group row">
                <label for="username" class="col-sm-2 col-form-label">Username</label>
                <div class="col-sm-10">
                    <input type="text" id="username" name="username" value="${user.getUsername()}" class="form-control">
                </div>
            </div>
            <#if isAdmin>
                <div class="form-group">
                    <div class="custom-control custom-switch">
                        <input type="checkbox" class="custom-control-input" id="isActiveSwitch"
                               name="isActive" <#if user.isActive()>checked</#if>
                        >
                        <label class="custom-control-label" for="isActiveSwitch">Active</label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="custom-control custom-switch">
                        <input type="checkbox" class="custom-control-input" id="isAdminSwitch"
                               name="isAdmin" <#if user.isAdmin()>checked</#if>
                        >
                        <label class="custom-control-label" for="isAdminSwitch">Admin rules</label>
                    </div>
                </div>
            </#if>

            <div class="form-group">
                <div class="custom-file">
                    <input type="file" id="imageFile" name="image" class="form-control">
                    <label class="custom-file-label" for="imageFile">Choose user's image file</label>
                </div>
            </div>

            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <div class="form-group">
                <button type="submit" class="btn btn-primary">
                    Save
                </button>
            </div>
        </form>
    </div>
</@c.page>