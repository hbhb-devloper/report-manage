package com.hbhb.cw.report.rpc;

import com.hbhb.cw.systemcenter.api.HallApi;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cn
 */
@FeignClient(value = "${provider.system-center}", url = "${feign-url}", contextId = "HallApiExp", path = "/hall")
public interface HallApiExp extends HallApi {
}
