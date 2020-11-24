<#macro filterForm url>
        <div class="form-row">
            <div class="form-group">
                <form method="get" action="${url}" class="form-inline">
                    <input class="form-control col-8" type="text" name="filter"
                           <#if filter??>value="${filter}"</#if> placeholder="Search by title or author">
                    <button type="submit" class="btn btn-primary col-auto ml-3">Search</button>
                </form>
            </div>
        </div>
</#macro>