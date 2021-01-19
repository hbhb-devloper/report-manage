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
    where r.type = #{cond.type}
    -- @if(isNotEmpty(cond.unitId)){
      and r.unit_id = #{cond.unitId}
    -- @}
    -- @if(isNotEmpty(cond.hallId)){
      and r.hall_id = #{cond.hallId}
    -- @}
    -- @if(isNotEmpty(cond.manageId)){
      and r.manage_id = #{cond.manageId}
    -- @}
    -- @if(isNotEmpty(cond.categoryId)){
      and r.category_id = #{cond.categoryId}
    -- @}
    -- @if(isNotEmpty(cond.state)){
      and r.state = #{cond.state}
    -- @}
    -- @if(isNotEmpty(cond.period)){
      and r.period = #{cond.period}
    -- @}
    -- @if(isNotEmpty(cond.relationName)){
      and concat(rm.manage_name,rc.report_name) like concat ('%',#{cond.relationName},'%')
    -- @}
    -- @pageIgnoreTag(){
      group by r.id
    -- @}
```