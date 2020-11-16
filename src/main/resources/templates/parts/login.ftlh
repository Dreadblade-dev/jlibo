<#macro login path isSignUpForm>
    <form action="${path}" method="post">
        <div>
            <input type="text" name="username" placeholder="Username">
        </div>
        <div>
            <input type="password" name="password" placeholder="Password">
        </div>

        <#if isSignUpForm>
            <div>
                <input type="password" name="password_confirmation" placeholder="Confirm password">
            </div>
        </#if>

        <input type="hidden" name="_csrf" value="${_csrf.token}">

        <div>
            <button type="submit">
                <#if isSignUpForm>
                    Sign up
                <#else>
                    Sign in
                </#if>
            </button>
        </div>
    </form>
    <#if isSignUpForm>
        <a href="/login">
            Already have an account? Sign in
        </a>
        <#else>
        <a href="/sign-up">
            Don't have an account? Sign up
        </a>
    </#if>
</#macro>

<#macro logout>
    <#include "security.ftlh">
    <form action="<#if user??>/logout<#else>/login</#if>" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">
            <#if user??>
                Sign out
                <#else>
                Sign in
            </#if>
        </button>
    </form>
</#macro>