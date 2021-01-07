package com.hbhb.cw.report.rpc;

import com.hbhb.cw.systemcenter.api.UnitApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${provider.system-center}", url = "${feign-url}", contextId = "UnitApi", path = "unit")
public interface UnitApiExp extends UnitApi {
}
