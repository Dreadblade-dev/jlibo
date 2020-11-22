<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="container justify-content-center align-items-center">
        <form method="post" enctype="multipart/form-data"
              action="/author/<#if author?? && author.getId()??>${author.getId()}/edit<#else>new</#if>">
            <div class="form-group row">
                <label for="name" class="col-sm-2 col-form-label">Author's name</label>
                <div class="col-sm-10">
                    <input type="text" id="name" placeholder="Author's name"
                           <#if author?? && author.getName()??>
                               value="${author.getName()}"
                           </#if>
                           name="name" class="form-control <#if nameIsInvalid??>is-invalid</#if>">
                    <#if nameIsInvalid??>
                        <div class="invalid-feedback">
                            ${nameIsInvalid}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label for="name" class="col-sm-2 col-form-label">Author's biography</label>
                <div class="col-sm-10">
                    <textarea name="description" class="form-control <#if descriptionIsInvalid??>is-invalid</#if>"
                              placeholder="Author's biography"
                    ><#if author?? && author.getDescription()??>${author.getDescription()}</#if></textarea>
                    <#if descriptionIsInvalid??>
                        <div class="invalid-feedback">
                            ${descriptionIsInvalid}
                        </div>
                    </#if>
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
                <button type="submit" class="btn btn-primary mb-2">
                    Save
                </button>
                <#if author?? && author.getId()??>
                    <button type="submit" formaction="/author/${author.getId()}/delete"
                            class="btn btn-danger mb-2 float-right">
                        Delete
                    </button>
                </#if>
            </div>
        </form>
    </div>
</@c.page>