selectPageByCond
===
```sql
    select 
        -- @pageTag(){
           r.id           as id,
           r.period       as period,
           r.has_biz      as hasBiz,
           CONCAT(rm.manage_name, rc.report_name,'审批流程') as relationName,
           rm.manage_name as manageName,
           rc.report_name as reportName,
           r.has_biz      as hasBiz,
           r.founder      as founder,
           r.launch_time  as launchTime,
           r.unit_id      as unitId,
           r.hall_id      as hallId,
           r.state        as state
        -- @}
    from report r
             left join report_manage rm on r.manage_id = rm.id
             left join report_category rc on r.category_id = rc.id
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
    -- @if(isNotEmpty(cond.periodInfo)){
      and r.period_info = #{cond.periodInfo}
    -- @}
    -- @if(isNotEmpty(cond.launchTime)){
      and r.launch_time = #{cond.launchTime}
    -- @}
    -- @if(isNotEmpty(cond.relationName)){
      and concat(rm.manage_name,rc.report_name) like concat ('%',#{cond.relationName},'%')
    -- @}
```

selectListByCond
===
```sql
    select 
           r.id           as id,
           r.period       as period,
           r.has_biz      as hasBiz,
           CONCAT(rm.manage_name, rc.report_name,'审批流程') as relationName,
           rm.manage_name as manageName,
           rc.report_name as reportName,
           r.has_biz      as hasBiz,
           r.founder      as founder,
           r.launch_time  as launchTime,
           r.unit_id      as unitId,
           r.hall_id      as hallId,
           r.state        as state
    from report r
             left join report_manage rm on r.manage_id = rm.id
             left join report_category rc on r.category_id = rc.id
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
    -- @if(isNotEmpty(cond.periodInfo)){
      and r.period_info = #{cond.periodInfo}
    -- @}
    -- @if(isNotEmpty(cond.launchTime)){
      and r.launch_time = #{cond.launchTime}
    -- @}
    -- @if(isNotEmpty(cond.relationName)){
      and concat(rm.manage_name,rc.report_name) like concat ('%',#{cond.relationName},'%')
    -- @}
group by r.id;
```