selectPropertyListByCategoryId
===
```sql
select
-- @pageTag(){
       rp.id                               as id,
       rc.report_name                      as categoryName,
       category_id                         as categoryId,
       scope                               as scope,
       period                              as period,
       flow_type_id                        as flowTypeId,
       flow_id                             as flowId,
       date_format(start_time, '%Y-%m-%d') as startTime,
       date_format(end_time, '%Y-%m-%d')   as endTime
 -- @}
from report_property rp
 left join report_category rc on rp.category_id = rc.id
    -- @where(){
    -- @if(isNotEmpty(categoryId)){
        category_id = #{categoryId}
    -- @}
    -- @}
```