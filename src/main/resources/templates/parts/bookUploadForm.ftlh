<a class="btn btn-primary mb-3 mt-2" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Upload book
</a>

<div class="collapse" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data" action="/books">
            <div class="form-group">
                <input type="text" placeholder="Title" name="title" class="form-control">
            </div>
            <div class="form-group">
                <input type="text" placeholder="Author" name="author" class="form-control">
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" id="imageFile" name="image" class="form-control-file">
                    <label class="custom-file-label" for="imageFile">Choose image</label>
                </div>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" id="bookFile" name="book" class="form-control-file">
                    <label class="custom-file-label" for="bookFile">Choose file (.pdf)</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="form-group">
                <button type="submit" class="btn btn-primary mb-2">Upload book</button>
            </div>
        </form>
    </div>
</div>