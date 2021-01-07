package com.hbhb.cw.report.rpc;

import com.hbhb.cw.systemcenter.api.FileApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${provider.system-center}", url = "${feign-url}", contextId = "FileApi", path = "file")
public interface FileApiExp extends FileApi {
}
