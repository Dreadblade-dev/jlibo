<#macro bookList filterUrl>
    <#include "security.ftlh">
    <#import "pager.ftlh" as p>
    <#import "bookFilter.ftlh" as f>

    <#if book??>
        <@p.pager url page/>
    </#if>
    <#if page.getContent()??>
        <#if page.getContent()?size gt 0>
            <@f.filterForm filterUrl/>

            <div class="container justify-content-center align-items-center card-columns" id="books-list">
                <#list page.getContent() as book>
                    <#if book??>
                        <div class="card" style="width: 18rem;" data-id="${book.getId()}">
                            <a href="/book/${book.getId()}">
                                <img src="/images/book/${book.getImageFilename()}" class="card-img-top"/>
                            </a>
                            <div class="card-body">
                                <h5 class="card-title">
                                    ${book.getTitle()}
                                    <#if isAdmin>
                                        <a href="/book/${book.getId()}/edit" class="btn btn-primary">Edit</a>
                                    </#if>
                                </h5>
                                <p class="card-text">
                                    <a href="/author/${book.getAuthor().getId()}">
                                        ${book.getAuthor().getName()}
                                    </a>
                                </p>
                                <p class="card-text">
                                    Uploaded by:
                                    <a href="/user/${book.getUploadedBy().getId()}">
                                        ${book.getUploadedBy().getUsername()}
                                    </a>
                                </p>
                                <#if isSuggestedPage??>
                                    <form method="post" class="card-text row justify-content-between">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                        <button formaction="/book/${book.getId()}/accept" class="btn btn-success col-4 ml-2">
                                            Accept
                                        </button>
                                        <button formaction="/book/${book.getId()}/decline" class="btn btn-danger col-4 mr-2">
                                            Decline
                                        </button>
                                    </form>
                                </#if>
                            </div>
                        </div>
                    </#if>
                </#list>
            </div>
        <#else>
            <h5>There are no books</h5>
        </#if>
    </#if>
    <#if book??>
        <@p.pager url page/>
    </#if>
</#macro>

