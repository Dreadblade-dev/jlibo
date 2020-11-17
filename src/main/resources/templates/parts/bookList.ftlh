<#if books??>
    <#list books as book>
        <div>
            <a href="/books/${book.getId()}">
                <img src="/images/${book.getImageFilename()}">
            </a>
            <p>${book.getTitle()}</p>
            <p>
                <a href="/author/${book.getAuthor().getId()}">
                    ${book.getAuthor().getName()}
                </a>
            </p>
            <p>Uploaded by: ${book.getUploadedBy().getUsername()}</p>
        </div>
    </#list>
</#if>