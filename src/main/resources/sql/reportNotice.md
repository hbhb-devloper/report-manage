selectPageByCond
===
 ```sql
    select
    -- @pageTag(){
       rn.id                                            as id,
        rn.report_id                                      as businessId,
        rn.content                                       as content,
        rn.flow_type_id                                  as flowTypeId,
        r.user_id                                        as userName,
        r.unit_id                                        as unitId,
        date_format(rn.create_time, '%Y-%m-%d %H:%i:%s') as createTime,
        r.state     
    -- @}
    from report_notice rn
             left join report r on rn.report_id = r.id
    where rn.state = 0
    -- @if(isNotEmpty(cond.userId)){
        and rn.receiver = #{cond.userId}
    -- @}
    -- @pageIgnoreTag(){
        order by rn.create_time desc, rn.state
    -- @}
```