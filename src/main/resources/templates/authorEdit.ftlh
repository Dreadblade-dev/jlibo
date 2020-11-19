<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container justify-content-center align-items-center">
        <form method="post" action="/author/${author.getId()}/edit">
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
                    <input type="file" id="photoFile" placeholder="Author's photo" name="photo" class="form-control">
                    <label class="custom-file-label" for="photoFile">Choose author's photo file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="form-group">
                <button type="submit" class="btn btn-primary mb-2">Add</button>
            </div>
        </form>
    </div>
</@c.page>