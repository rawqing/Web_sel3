package yq.test.webSel3.handler.matches

import org.hamcrest.Matcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.openqa.selenium.WebElement
import yq.test.webSel3.handler.isExist

object WebElementMatchers {

    fun isDisplayed(): Matcher<WebElement> {
        return object: BaseMatcher<WebElement>(){
            override fun matches(item: Any?): Boolean {
                if (item is WebElement) {
                    return item.isExist()
                }
                return false
            }
            override fun describeTo(description: Description?) {
                description?.appendText("is displayed.")
            }

        }
    }

    fun hasContainText(text: String): Matcher<WebElement> {
        return object: BaseMatcher<WebElement>(){
            override fun matches(item: Any?): Boolean {
                if (item is WebElement) {
                    return item.text.contains(text)
                }
                return false
            }
            override fun describeTo(description: Description?) {
                description?.appendText("has contain text $text.")
            }

        }
    }

}