package com.example.demo.excel.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.excel.workbook.WorkbookType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class ExcelDownloadPolicyTest {

    private final ApplicationContextRunner contextRunner =
        new ApplicationContextRunner().withUserConfiguration(ExcelDownloadPolicy.class);

    @Test
    void shouldApplyConfiguredMaxRowsByWorkbookType() {
        contextRunner
            .withPropertyValues(
                "excel.download.max-rows.xssf=111",
                "excel.download.max-rows.sxssf=222"
            )
            .run(context -> {
                ExcelDownloadPolicy policy = context.getBean(ExcelDownloadPolicy.class);

                assertThat(policy.maxRows(WorkbookType.XSSF)).isEqualTo(111);
                assertThat(policy.maxRows(WorkbookType.SXSSF)).isEqualTo(222);
            });
    }

    @Test
    void shouldUseDefaultMaxRowsWhenPropertyIsMissing() {
        contextRunner.run(context -> {
            ExcelDownloadPolicy policy = context.getBean(ExcelDownloadPolicy.class);

            assertThat(policy.maxRows(WorkbookType.XSSF)).isEqualTo(30_000);
            assertThat(policy.maxRows(WorkbookType.SXSSF)).isEqualTo(300_000);
        });
    }
}
