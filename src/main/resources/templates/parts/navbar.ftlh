<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light sticky-top">
    <a class="navbar-brand" href="/">Jlibo</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Books</a>
            </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/author/new">Add new author</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/authors-list">Authors list</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/users-list">Users list</a>
                </li>
            </#if>
        </ul>
        <div class="navbar-text mr-3">
            You are currently logged in as
            <#if currentUser??>
                <a href="/user/${currentUserId}">${username}</a>
            <#else>
                ${username}
            </#if>
        </div>
      <@l.logout/>
    </div>
</nav>