package com.example.demo.excel.workbook;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class ExcelWorkbookFactoryProfileTest {

    private final ApplicationContextRunner contextRunner =
        new ApplicationContextRunner()
            .withUserConfiguration(XssfWorkbookFactory.class, SxssfWorkbookFactory.class);

    @Test
    void shouldUseXssfFactoryByDefaultProfile() {
        contextRunner.run(context -> {
            ExcelWorkbookFactory factory = context.getBean(ExcelWorkbookFactory.class);

            assertThat(factory).isInstanceOf(XssfWorkbookFactory.class);
            assertThat(factory.workbookType()).isEqualTo(WorkbookType.XSSF);
        });
    }

    @Test
    void shouldUseSxssfFactoryWhenExcelSxssfProfileIsActive() {
        contextRunner
            .withPropertyValues("spring.profiles.active=excel-sxssf")
            .run(context -> {
                ExcelWorkbookFactory factory = context.getBean(ExcelWorkbookFactory.class);

                assertThat(factory).isInstanceOf(SxssfWorkbookFactory.class);
                assertThat(factory.workbookType()).isEqualTo(WorkbookType.SXSSF);
            });
    }
}
