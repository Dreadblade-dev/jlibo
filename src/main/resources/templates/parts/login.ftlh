<#macro login path isSignUpForm>
    <div class="container justify-content-center align-items-center col-md-4">
        <form action="${path}" method="post" class="form-group" <#if isSignUpForm>enctype="multipart/form-data"</#if>>
            <div class="form-group">
                <input type="text" name="username" placeholder="Username"
                       class="form-control <#if usernameIsInvalid??>is-invalid</#if>"
                       <#if user?? && user.getUsername()??>value="${user.getUsername()}"</#if>>
                <#if usernameIsInvalid??>
                    <div class="invalid-feedback">
                        ${usernameIsInvalid}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <input type="password" name="password" placeholder="Password"
                       class="form-control <#if passwordIsInvalid??>is-invalid</#if>">
                <#if passwordIsInvalid??>
                    <div class="invalid-feedback">
                        ${passwordIsInvalid}
                    </div>
                </#if>
            </div>

            <#if isSignUpForm>
                <div class="form-group">
                    <input type="password" name="password_confirmation" placeholder="Confirm password"
                           class="form-control <#if passwordConfirmIsInvalid??>is-invalid</#if>">
                    <#if passwordConfirmIsInvalid??>
                        <div class="invalid-feedback">
                            ${passwordConfirmIsInvalid}
                        </div>
                    </#if>
                </div>

                <div class="form-group">
                    <input type="email" name="email" placeholder="email@example.com" class="form-control
                           <#if emailIsInvalid??>is-invalid</#if>"<#if user?? && user.getEmail()??>
                                value="${user.getEmail()}"
                           </#if>>
                    <#if emailIsInvalid??>
                        <div class="invalid-feedback">
                            ${emailIsInvalid}
                        </div>
                    </#if>
                </div>

                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" id="imageFile" name="image" class="form-control">
                        <label class="custom-file-label" for="imageFile">Choose image file</label>
                    </div>
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
    <#if currentUser??>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary">
                Sign out
            </button>
        </form>
        <#else>
        <a href="/login" class="btn btn-primary">Sign in</a>
    </#if>
</#macro>