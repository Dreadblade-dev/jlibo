<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container justify-content-center align-items-center">
        <form method="post" enctype="multipart/form-data" action="/author/${author.getId()}/edit">
            <div class="form-group">
                <input type="text" placeholder="Author's name"
                       <#if author.getName() != "<author's name>">
                           value="${author.getName()}"
                       </#if>
                       name="name" class="form-control">
            </div>
            <div class="form-group">
                <input type="text" placeholder="Author's biography"
                        <#if author.getName() != "<<author's name>'s biography>">
                            value="${author.getDescription()}"
                        </#if>
                       name="description" class="form-control">
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" id="imageFile" placeholder="Author's photo" name="image" class="form-control">
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