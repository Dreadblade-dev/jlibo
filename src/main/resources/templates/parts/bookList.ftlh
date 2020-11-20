<#if books??>
    <#if books?size gt 0>
        <div class="container justify-content-center align-items-center card-columns">
            <#list books as book>
                <div class="card" style="width: 18rem;">
                    <a href="/book/${book.getId()}">
                        <img src="/images/book/${book.getImageFilename()}" class="card-img-top">
                    </a>
                    <div class="card-body">
                        <h5 class="card-title">${book.getTitle()}</h5>
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
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <h3>There are no books</h3>
    </#if>
</#if>