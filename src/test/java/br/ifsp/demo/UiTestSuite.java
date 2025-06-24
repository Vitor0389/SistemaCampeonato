package br.ifsp.demo;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags("UiTest")
@SelectPackages("br.ifsp.demo")
public class UiTestSuite {
}
