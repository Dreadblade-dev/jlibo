<#list books as book>
    <div>
        <a href="/books/${book.getId()}">
            <img src="/images/${book.getImageFilename()}">
        </a>
        <p>${book.getTitle()}</p>
        <p>${book.getAuthor()}</p>
    </div>
</#list>