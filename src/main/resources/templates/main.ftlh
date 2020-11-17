<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<#include "parts/security.ftlh">
<@c.page>
    <@l.logout/>
    <h1>Java Library Online</h1>
    <h2>Welcome, <#if username??>${username}<#else>guest</#if>!</h2>

    <#if message??>
        <p>${message}</p>
    </#if>

    <form method="post" enctype="multipart/form-data" action="/books">
        <div>
            <input type="text" placeholder="Title" name="title">
        </div>
        <div>
            <input type="text" placeholder="Author" name="author">
        </div>
        <div>
            <input type="file" id="imageFile" name="image">
            <label for="imageFile">Choose image</label>
        </div>
        <div>
            <input type="file" id="bookFile" name="book">
            <label for="bookFile">Choose file (.pdf)</label>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div>
            <button type="submit">Upload book</button>
        </div>
    </form>

    <#include "parts/bookList.ftlh">
</@c.page>