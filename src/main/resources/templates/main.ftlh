<#import "parts/common.ftlh" as c>
<#import "parts/bookFilter.ftlh" as f>

<@c.page>
    <@f.filterForm ""/>
    <#include "parts/bookList.ftlh">
</@c.page>