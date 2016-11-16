package com.yzdc.in;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;

/**
 * Desc:    集成testNG进行单元测试
 * Author: Iron
 * CreateDate: 2016-10-20
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
@ContextConfiguration("classpath:testng-context.xml")
public abstract class AbstractTestNGTest extends AbstractTestNGSpringContextTests {

    /**
     * Initializes the test context.
     */
    @BeforeSuite(alwaysRun = true)
    public void init() {
    }
}
