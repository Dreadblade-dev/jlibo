<#import "parts/common.ftlh" as c>
<#import "parts/bookFilter.ftlh" as f>

<@c.page>
    <div class="container justify-content-center align-items-center mb-3">
        <div class="card" style="width: 18rem;">
            <a href="/book/${book.getId()}">
                <img src="/images/book/${book.getImageFilename()}" class="card-img-top">
            </a>
            <div class="card-body">
                <h5 class="card-title">
                    ${book.getTitle()}
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
            </div>
        </div>
    </div>

    <form method="post" enctype="multipart/form-data">
        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">Book's title</label>
            <div class="col-sm-10">
                <input type="text" id="name" placeholder="Book's title" value="${book.getTitle()}"
                       name="title" class="form-control <#if titleIsInvalid??>is-invalid</#if>">
                <#if titleIsInvalid??>
                    <div class="invalid-feedback">
                        ${titleIsInvalid}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group mb-3">
            <div class="custom-file">
                <input type="file" id="imageFile" name="imageFile"
                       class="custom-file-input <#if imageFilenameIsInvalid??>is-invalid</#if>">
                <label class="custom-file-label" for="imageFile">Choose book's cover image</label>
                <#if imageFilenameIsInvalid??>
                    <div class="invalid-feedback">
                        ${imageFilenameIsInvalid}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group mb-3">
            <div class="custom-file">
                <input type="file" id="bookFile" name="bookFile"
                       class="custom-file-input <#if bookFilenameIsInvalid??>is-invalid</#if>">
                <label class="custom-file-label" for="bookFile">Choose file (.pdf)</label>
                <#if bookFilenameIsInvalid??>
                    <div class="invalid-feedback">
                        ${bookFilenameIsInvalid}
                    </div>
                </#if>
            </div>
        </div>
        <input type="hidden" name="author_id" value="${book.getAuthor().getId()}">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div class="form-group">
            <button type="submit" formaction="/book/${book.getId()}/edit"
                    class="btn btn-primary mb-2">Save</button>
            <button type="submit" formaction="/book/${book.getId()}/delete"
                    class="btn btn-danger float-right mb-2">Delete</button>
        </div>
    </form>
</@c.page>