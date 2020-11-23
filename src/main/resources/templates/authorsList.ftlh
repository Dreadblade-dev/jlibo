<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <table class="table table-hover table-responsive-md">
        <thead>
        <th scope="col">Id</th>
        <th scope="col">Name</th>
        <th scope="col">Description</th>
        <th scope="col">Books</th>
        <th scope="col">Edit</th>
        </thead>
        <#list authors as author>
            <tr>
                <td>${author.getId()}</td>
                <td><a href="/author/${author.getId()}">${author.getName()}</a></td>
                <td>${author.getDescription()}</td>
                <td>
                    <#list author.getBooks() as book>
                        <a href="/book/${book.getId()}/edit">
                            ${book.getTitle()}
                        </a>&nbsp
                    </#list>
                </td>
                <td><a href="/author/${author.getId()}/edit" class="btn btn-primary btn-sm">Edit</a></td>
            </tr>
        </#list>
    </table>
</@c.page>