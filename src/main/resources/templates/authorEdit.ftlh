<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container justify-content-center align-items-center">
        <form method="post" enctype="multipart/form-data" action="/author/${author.getId()}/edit">
            <div class="form-group row">
                <label for="name" class="col-sm-2 col-form-label">Author's name</label>
                <div class="col-sm-10">
                    <input type="text" id="name" placeholder="Author's name"
                           <#if author.getName() != "<author's name>">
                               value="${author.getName()}"
                           </#if>
                           name="name" class="form-control">
                </div>
            </div>
            <div class="form-group row">
                <label for="name" class="col-sm-2 col-form-label">Author's biography</label>
                <div class="col-sm-10">
                    <textarea name="description" class="form-control" placeholder="Author's biography"
                    ><#if author.getDescription() != "<<author's name>'s biography>"
                        >${author.getDescription()}</#if></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" id="imageFile" name="image" class="form-control">
                    <label class="custom-file-label" for="imageFile">Choose author's photo file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="form-group">
                <button type="submit" class="btn btn-primary mb-2">Save</button>
            </div>
        </form>
    </div>
</@c.page>