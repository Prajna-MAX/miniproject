package org.zeta;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.zeta.service.implementation.ProjectManagerService;

@Suite
@SelectClasses({
        org.zeta.AuthServiceTest.class,
        org.zeta.ClientServiceTest.class,
        org.zeta.ProjectManagerTest.class
})
public class TestSuite {
}


