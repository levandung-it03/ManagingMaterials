package com.CSDLPT.ManagingMaterials.database;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageObject {
    private Integer page = 1;
    private Integer size = 1;

    /**Spring MVC: Get the "page" param in HttpServletRequest, customize and return as PageObject.**/
    public PageObject(HttpServletRequest request) {
        //--Get the PageNumber from request.params if it's existing (page.default = 1).
        try { this.page = Integer.parseInt(request.getParameter("page")); }
        catch (NumberFormatException ignored) {}
    }

    public PageObject(Integer page) {
        this.page = page;
    }
}
