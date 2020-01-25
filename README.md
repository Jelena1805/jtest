**Automation testing solution**

Project is Java Maven project. I used Selenium with testNg.
It is possible to run test by test or as test suit (Chrome, Firefox).

**Installation**:
Import to eclipse as java maven project
All needed libraries are in pom.xml, so it easy to download it
Selenium web drivers are attached in folder

To run test you need to start selenium grid first
run grid:

java -jar selenium-server-standalone-3.141.59.jar -role hub

java -Dwebdriver.chrome.driver="/Users/jelenastanojevic/Documents/drivers/chromedriver.mac" -jar selenium-server-standalone-3.141.59.jar -role webdriver -hub [http://[use](http://[use) your ip]/grid/register -port 5566

**I made two classes**
JustEatSearchTest.java (contents two tests)
searchForDishOrRestorantFromResultPageTest.java (contents one test)

**Two run test suit: right click on :**
/selenium_mvn/src/main/resources/parallel-chrome.xml
/selenium_mvn/src/main/resources/parallel-firefox.xml
