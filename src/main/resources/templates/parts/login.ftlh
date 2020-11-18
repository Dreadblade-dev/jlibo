<#macro login path isSignUpForm>
    <div class="container justify-content-center align-items-center col-md-4">
        <form action="${path}" method="post" class="form-group ">
            <div class="form-group">
                <input type="text" name="username" placeholder="Username" class="form-control">
            </div>

            <div class="form-group">
                <input type="password" name="password" placeholder="Password" class="form-control">
            </div>

            <#if isSignUpForm>
                <div class="form-group">
                    <input type="password" name="password_confirmation" placeholder="Confirm password" class="form-control">
                </div>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

            <div class="form-group">
                <button type="submit" class="btn btn-primary">
                    <#if isSignUpForm>
                        Sign up
                    <#else>
                        Sign in
                    </#if>
                </button>
            </div>
        </form>
        <#if isSignUpForm>
            <a href="/login" class="btn btn-link">
                Already have an account? Sign in
            </a>
            <#else>
            <a href="/sign-up" class="btn btn-link">
                Don't have an account? Sign up
            </a>
        </#if>
    </div>
</#macro>

<#macro logout>
    <#include "security.ftlh">
    <form action="<#if user??>/logout<#else>/login</#if>" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">
            <#if user??>
                Sign out
                <#else>
                Sign in
            </#if>
        </button>
    </form>
</#macro>