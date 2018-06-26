package yq.test.webSel3.handler

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep


val log: Logger = LoggerFactory.getLogger("default log")
lateinit var webDriver: WebDriver

fun WebElement._click(): WebElement{
    log.debug("_click {}",this.toString())
    try {
        this.click()
    } catch (e: Exception) {
        screenshot(scene = "_click()")
        e.printStackTrace()
        throw e
    }
    return this
}
fun WebElement._sendKeys(string: String): WebElement{
    log.debug("_sendKeys {}",this.toString())
    try {
        this.clear()
        this.sendKeys(string)
    } catch (e: Exception) {
        screenshot(scene = "_sendKeys($string)")
        e.printStackTrace()
        throw e
    }
    return this
}
fun WebElement._select(): WebElement{
    log.debug("_select ${this}")
    try {
        if (this.isSelected) return this

        this._click()
    } catch (e: Exception) {
        screenshot(scene = "_select()")
        e.printStackTrace()
        throw e
    }
    return this
}
fun WebElement._deselect(): WebElement{
    log.debug("_deselect ${this}")
    try {
        if (!this.isSelected) return this

        this._click()
    } catch (e: Exception) {
        screenshot(scene = "_deselect()")
        e.printStackTrace()
        throw e
    }
    return this
}
/**
 * 等待 , 默认次数 5 次 , 时间间隔按 BTest 中的隐式等待
 * !!!! 调试过程中 改等待次数为 1
 */
fun WebElement.waitFor(condition:(it:WebElement)->Boolean ,times:Int = 1 ,preWait: Long = 0): WebElement{
    if (preWait > 0)  sleep(preWait)
    screenshot(scene = "before waitFor")
    for (i in 1..times) {
        log.debug("wait for $this $i times.")
        if (condition(this)) {
            return this
        }
    }
    return this
}

fun WebElement.isExist():Boolean{
    return try {
        this.isDisplayed
        log.debug("${this} is exist.")
        true
    } catch (e: org.openqa.selenium.NoSuchElementException) {
        log.debug("${this} does not exist.")
        false
    }
}
fun WebElement.isExist(times: Int):Boolean{
    for (i in 1..times) {
        if (this.isExist())
            log.debug("${this} is exist with $i times.")
            return true
    }
    log.debug("${this} does not exist with $times times.")
    return false
}
fun WebElement.isVisible(times: Int = 1):Boolean{
    for (i in 1..times) {
        try {
            if (this.isDisplayed) {
                log.debug("${this} is visible with $i times.")
                return true
            }
        } catch (e: Exception) {
            log.debug("${this} does not visible with $i times.")
        }
    }
    return false
}

fun WebElement._findElement(by: By): WebElement? {
    return try {
        val fe =this.findElement(by)
        fe
    } catch (e: org.openqa.selenium.NoSuchElementException) {
        null
    }
}

fun  WebElement._check(matcher: Matcher<WebElement> ,times: Int = 2):WebElement{
    log.debug("_check ${this.toString().limit(100)} $times times.")
    for (i in 1..times) {
        try {
            MatcherAssert.assertThat(this, matcher)
            return this
        } catch (t: Throwable) {
            if (i == 1) {
                screenshot(scene = "_check($matcher) $i times.")
            }else if (i == times) {
                screenshot(scene = "_check($matcher) $i times.")
                throw t
            }
        }
        sleep(1000)
    }
    return this
}
fun <T> check(o:()-> T, matcher: Matcher<T>, times: Int = 2) {
    log.debug("_check ${o.toString().limit(100)} ($matcher) $times times.")
    for (i in 1..times) {
        var the:T? = null
        try {
            the = o()
            MatcherAssert.assertThat(the ,matcher)
            return
        } catch (t: Throwable) {
            log.debug("_check ${the?.toString()?.limit(10)}($matcher) $i times failure .")
            if (i == 1) {
                screenshot(scene = "_check($matcher) $i times.")
            }else if (i == times) {
                screenshot(scene = "_check($matcher) $i times.")
                throw t
            }
        }
        sleep(1000)
    }
}

/**
 * 限制字符串长度
 */
fun String.limit(length: Int ,more: String = " ..."): String {
    if (this.length <= length) {
        return this
    }
    return this.slice(0 until length) + more
}
