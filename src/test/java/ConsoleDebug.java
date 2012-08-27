import java.util.ArrayList;
import java.util.List;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mozilla.seleniumgrid.servlet.Console2;


public class ConsoleDebug {

  private Hub h;
  private RemoteProxy node;

  @BeforeClass
  public void setup() throws Exception {
    GridHubConfiguration c = new GridHubConfiguration();
    c.setTimeout(0);
    List<String> servlets = new ArrayList<String>();
    servlets.add(Console2.class.getCanonicalName());
    c.setServlets(servlets);
    c.setPort(4444);
    h = new Hub(c);
    h.start();

    RegistrationRequest request = new RegistrationRequest();
    request.getConfiguration().put(RegistrationRequest.REMOTE_HOST, "http://ipOfMyNode:5555");
    node = new DefaultRemoteProxy(request, h.getRegistry());
    h.getRegistry().add(node);
  }



  @Test
  public void smokeTest() {
    WebDriver d = null;
    try {
      d = new FirefoxDriver();
      d.get("http://localhost:4444/grid/admin/Console2");
      String title = d.getTitle();
      Assert.assertEquals(title, "Selenium Grid Console2");
    } finally {
      if (d != null) {
        d.quit();
      }
    }
  }

  @AfterClass
  public void teardown() throws Exception {
    h.stop();
  }

}
