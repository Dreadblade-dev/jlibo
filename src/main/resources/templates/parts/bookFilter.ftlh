<#macro filterForm url>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="${url}/filter" class="form-inline">
                <input class="form-control" type="text" name="filter"
                       <#if filter??>value="${filter}"</#if> placeholder="Search by title or author">
                <button type="submit" class="btn btn-primary ml-3">Search</button>
            </form>
        </div>
    </div>
</#macro>