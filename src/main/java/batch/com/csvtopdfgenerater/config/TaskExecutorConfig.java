package batch.com.csvtopdfgenerater.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TaskExecutorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutorConfig.class);

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor() {
            private static final long serialVersionUID = 1L;

            @Override
            public void execute(Runnable task) {
                super.execute(() -> {
                    LOGGER.info("Executing with thread: " + Thread.currentThread().getName());
                    task.run();
                });
            }
        };
    }
}

/*
 * # General Environment Configuration environment=Project
 * 
 * # Datasource for data_rw
 * spring.datasource.data-rw.driver-class-name=com.microsoft.sqlserver.jdbc.
 * SQLServerDriver
 * spring.datasource.data-rw.url=jdbc:sqlserver://dev:1433;database=dev
 * spring.datasource.data-rw.username=sdf spring.datasource.data-rw.password=adf
 * 
 * # Datasource for log
 * spring.datasource.log.driver-class-name=com.microsoft.sqlserver.jdbc.
 * SQLServerDriver
 * spring.datasource.log.url=jdbc:sqlserver://STYXDev2:1433;database=wbe
 * spring.datasource.log.username=javv spring.datasource.log.password=abd
 * 
 * # Datasource for log d
 * spring.datasource.log-d.driver-class-name=com.microsoft.jdbc.sqlserver.
 * SQLServerDriver
 * spring.datasource.log-d.url=jdbc:sqlserver://abdd:1433;database=we2
 * spring.datasource.log-d.username=javaint
 * spring.datasource.log-d.password=abdc
 * 
 * # Datasource for logg
 * spring.datasource.logg.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.logg.url=jdbc:oracle:thin:@bad:1314:king
 * spring.datasource.logg.username=ewew spring.datasource.logg.password=pdadda
 * 
 * # Datasource for bac
 * spring.datasource.bac.driver-class-name=com.fd.jdbc.IfxDriver
 * spring.datasource.bac.url=jdbc:ifx://abfafafdf:1233/fabdf
 * spring.datasource.bac.username=afa spring.datasource.bac.password=agaga
 * 
 * # Datasource for afdf
 * spring.datasource.afdf.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.afdf.url=jdbc:oracle:thin:@fafa.org:1232:ckingdev
 * spring.datasource.afdf.username=fdf spring.datasource.afdf.password=gdgd
 * 
 * # Datasource for dg
 * spring.datasource.dg.driver-class-name=com.microsoft.jdbc.sqlserver.
 * SQLServerDriver
 * spring.datasource.dg.url=jdbc:sqlserver://fdfdfd:1433;database=WebAdppDev2
 * spring.datasource.dg.username=fdf spring.datasource.dg.password=ddgd
 * 
 * # Datasource for ** fdfd'
 * spring.datasource.fdfd.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.fdfd.url=jdbc:oracle:thin:@edblx12:11333:fdfd
 * spring.datasource.fdfd.username=webdarw spring.datasource.fdfd.password=dfgd
 * 
 * # Datasource for dfdfd
 * spring.datasource.dfdfd.driver-class-name=com.microsoft.jdbc.sqlserver.
 * SQLServerDriver
 * spring.datasource.dfdfd.url=jdbc:sqlserver://fdfdfd:1433;database=gdgdgd
 * spring.datasource.dfdfd.username=fdfdfdgd
 * spring.datasource.dfdfd.password=fddfd
 * 
 * # Datasource for fdf spring.datasource.fdf.driver-class-name=$(gdg}
 * spring.datasource.fdf.url=jdbc:$(gdg}
 * spring.datasource.fdf.username=${fdgdgd}
 * spring.datasource.fdf.password=${gsga}
 * 
 * # Datasource for gdgdgdga
 * spring.datasource.gdgdgdga.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.gdgdgdga.url=jdbc:oracle:thin:@gagddgd:421412:dafda
 * spring.datasource.gdgdgdga.username=fadfa
 * spring.datasource.gdgdgdga.password=gagds®s
 * 
 * # Datasource for agdsg
 * spring.datasource.agdsg.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.agdsg.url=jdbc:oracle:thin:@adg:32114:kingdev
 * spring.datasource.agdsg.username=fad spring.datasource.agdsg.password=dagds
 * 
 * # Datasource for oid
 * spring.datasource.oid.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.oid.url=jdbc:oracle:thin:@fadfa.king.org:dfsaa:fad
 * spring.datasource.oid.username=afds spring.datasource.oid.password=fadsf
 * 
 * # Datasource for tca_rw
 * spring.datasource.tca-rw.driver-class-name=oracle.jdbc.driver.OracleDriver
 * spring.datasource.tca-rw.url=jdbc:oracle:thin:@dfasafasd.king.org:123312:
 * ebsdev2 spring.datasource.tca-rw.username=afads
 * spring.datasource.tca-rw.password=adfdsfa®s
 * 
 * # Datasource for fasd
 * spring.datasource.fasd.driver-class-name=oracle.jdbc.OracleDriver
 * spring.datasource.fasd.url=jdbc:oracle:thin:@edblx12.king.org:1232:afds
 * spring.datasource.fasd.username=fafdas
 * spring.datasource.fasd.password=dasdfsa
 * 
 * # Web Services Endpoints webservices.wli.server=epluto05
 * webservices.wli.port=7011
 * 
 * rfverification.endpoint=afsd/service/fsda/dfas.jws
 * taskworker.endpoint=afsd/service/afsd/fsda.jws
 * firstprocess.endpoint=afds/service/rafsd/fads.jpd
 * 
 * # Documentum Configuration documentum.content_workflow.username=${adfs}
 * documentum.content_workflow.password=${afsd}
 * documentum.content_workflow.tableNameSingle=${afd}
 * 
 * # Weblogic Server Configuration weblogicserver.connection.provider=${url}
 * weblogicserver.connection.principal=${user}
 * weblogicserver.connection.credential=${password}
 */

