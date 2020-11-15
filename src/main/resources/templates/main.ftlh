<#import "parts/common.ftlh" as c>
<@c.page>
    <h1>Java Library Online</h1>
    <h2>Welcome, <#if username??>${username}<#else>guest</#if>!</h2>

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
        <div>
            <button type="submit">Upload book</button>
        </div>
    </form>

    <#include "parts/bookList.ftlh">
</@c.page>