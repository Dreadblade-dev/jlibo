<div class="col-md-9">
<div class="container justify-content-center align-items-center">
    <a class="btn btn-primary mb-3 mt-2" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Upload book
    </a>
</div>

<div class="collapse" id="collapseExample">
    <div class="form-group mt-3 container justify-content-center align-items-center">
        <form method="post" enctype="multipart/form-data" action="/book/new">
            <div class="form-group">
                <input type="text" placeholder="Title" name="title" <#if title??>value="${title}"</#if>
                       class="form-control <#if titleIsInvalid??>is-invalid</#if>"/>
                <#if titleIsInvalid??>
                    <div class="invalid-feedback">
                        ${titleIsInvalid}
                    </div>
                </#if>
            </div>
            <div class="form-group mb-3">
                <div class="custom-file">
                    <input type="file" id="imageFile" name="imageFile"
                           class="custom-file-input <#if imageFilenameIsInvalid??>is-invalid</#if>"/>
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
                           class="custom-file-input <#if bookFilenameIsInvalid??>is-invalid</#if>"/>
                    <label class="custom-file-label" for="bookFile">Choose file (.pdf)</label>
                    <#if bookFilenameIsInvalid??>
                        <div class="invalid-feedback">
                            ${bookFilenameIsInvalid}
                        </div>
                    </#if>
                </div>
            </div>
            <input type="hidden" name="author_id" value="${author.getId()}"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group">
                <button type="submit" class="btn btn-primary mb-2">Upload book</button>
            </div>
        </form>
    </div>
</div>
</div>