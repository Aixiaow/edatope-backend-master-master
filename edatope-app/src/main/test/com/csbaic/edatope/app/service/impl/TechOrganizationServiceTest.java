package com.csbaic.edatope.app.service.impl;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author bage
 * @version 1.0
 * @date 2022/03/18 11:47
 * @since JDK 1.8
 */
@SpringBootTest
class TechOrganizationServiceTest {

    @Autowired
    public TechOrganizationService techOrganizationService;


    @Test
    void listOrganizationAdminRoleWithBizType() {
        techOrganizationService.listOrganizationAdminRoleWithBizType(Lists.newArrayList("D001-004"));
    }
}