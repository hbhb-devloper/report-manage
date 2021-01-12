selectListByCond
===
```sql
    select 
        -- @pageTag(){
           r.has_biz      as hasBiz,
           rm.manage_name as manageName,
           rc.report_name as reportName,
           r.has_biz      as hasBiz,
           r.founder      as founder,
           r.create_time  as createTime,
           r.state        as state
        -- @}
    from report r
             left join report_manage rm on r.manage_id = rm.id
             left join report_category rc on rm.id = rc.manage_id
    where r.type = #{type}
    -- @if(isNotEmpty(unitId)){
      and r.unit_id = #{unitId}
    -- @}
    -- @if(isNotEmpty(hallId)){
      and r.hall_id = #{hallId}
    -- @}
    -- @if(isNotEmpty(manageId)){
      and r.manage_id = #{manageId}
    -- @}
    -- @if(isNotEmpty(categoryId)){
      and r.category_id = #{categoryId}
    -- @}
    -- @if(isNotEmpty(state)){
      and r.state = #{state}
    -- @}
    -- @if(isNotEmpty(period)){
      and r.period = #{period}
    -- @}
    -- @if(isNotEmpty(relationName)){
      and concat(rm.manage_name,rc.report_name) like concat ('%',#{relationName},'%')
    -- @}
    -- @pageIgnoreTag(){
      group by r.id
    -- @}
```