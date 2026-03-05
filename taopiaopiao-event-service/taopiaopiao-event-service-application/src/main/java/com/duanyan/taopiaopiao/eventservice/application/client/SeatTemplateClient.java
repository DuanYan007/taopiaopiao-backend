package com.duanyan.taopiaopiao.eventservice.application.client;

import com.duanyan.taopiaopiao.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 座位模板服务客户端
 */
@FeignClient(name = "seat-template-service")
public interface SeatTemplateClient {

    /**
     * 根据模板ID获取座位布局
     *
     * @param templateId 模板ID
     * @return 布局数据
     */
    @GetMapping("/client/seat-templates/{templateId}/layout")
    Result<LayoutDataResponse> getLayoutByTemplateId(@PathVariable("templateId") Long templateId);

    /**
     * 布局数据响应
     */
    class LayoutDataResponse {
        private Long templateId;
        private String templateName;
        private Long venueId;
        private Integer layoutType;
        private String layoutData;

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public Long getVenueId() {
            return venueId;
        }

        public void setVenueId(Long venueId) {
            this.venueId = venueId;
        }

        public Integer getLayoutType() {
            return layoutType;
        }

        public void setLayoutType(Integer layoutType) {
            this.layoutType = layoutType;
        }

        public String getLayoutData() {
            return layoutData;
        }

        public void setLayoutData(String layoutData) {
            this.layoutData = layoutData;
        }
    }
}
