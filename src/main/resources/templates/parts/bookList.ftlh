<#if books??>
    <div class="card-columns">
        <#list books as book>
            <div class="card" style="width: 18rem;">
                <a href="/books/${book.getId()}">
                    <img src="/images/${book.getImageFilename()}" class="card-img-top">
                </a>
                <div class="card-body">
                    <h5 class="card-title">${book.getTitle()}</h5>
                    <p class="card-text">
                        <a href="/author/${book.getAuthor().getId()}">
                            ${book.getAuthor().getName()}
                        </a>
                    </p>
                    <p class="card-text">Uploaded by: ${book.getUploadedBy().getUsername()}</p>
                </div>

            </div>
        </#list>
    </div>
</#if>