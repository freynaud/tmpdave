import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mozilla.seleniumgrid.servlet.Console2;


public class Console2Test {

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
    // requests are empty by default. Adding a firefox slot.
    request.addDesiredCapability(DesiredCapabilities.firefox());

    // the node doesn't have to exist for the hub to be told it's there.
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


  @Test(dependsOnMethods="smokeTest")
  public void canSeeNode() {
    WebDriver d = null;
    try {
      d = new FirefoxDriver();
      d.get("http://localhost:4444/grid/admin/Console2");
     
      // check the node is rendered
      WebElement el = d.findElement(By.id(node.getId()));
      Assert.assertEquals(el.getText(),
          "I'm proxy http://ipOfMyNode:5555,number of active sessions : 0");

      // and shows used slots.
      fakeANewFirefoxSessionOnHub();

      // go to the hub again.
      d.get("http://localhost:4444/grid/admin/Console2");
      el = d.findElement(By.id(node.getId()));
      
      // check it's displayed.
      Assert.assertEquals(el.getText(),
          "I'm proxy http://ipOfMyNode:5555,number of active sessions : 1");

    } finally {
      if (d != null) {
        d.quit();
      }
    }
  }


  private void fakeANewFirefoxSessionOnHub() {
    node.getTestSlots().get(0)
        .getNewSession((Map<String, Object>) DesiredCapabilities.firefox().asMap());
  }

  @AfterClass
  public void teardown() throws Exception {
    h.stop();
  }

}
