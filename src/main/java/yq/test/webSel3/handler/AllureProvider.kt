package yq.test.webSel3.handler

import io.qameta.allure.Allure
import io.qameta.allure.Attachment
import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


/**
 * 打印电脑屏幕
 */
@Attachment(value = "Print screen", type = "image/png")
fun printScreen(){
    val image = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().getScreenSize()))
    try {
        ImageIO.write(image, "png", File(".\\Screenshots\\screen_robot.png"))
    } catch (e: IOException) {
        log.error("AllureProvider.kt: screenshot error! ",e)
    }
}

/**
 * 浏览器截图 , 不包括浏览器菜单栏和状态栏 以及 任何超出浏览器的部分
 */
fun screenshot(scene: String = "", driver: WebDriver = webDriver){
    log.warn("AllureProvider.kt: Will screenshot . Maybe test failure.")
    try {
        Allure.addByteAttachmentAsync("Screenshot${if (scene == "") "" else " with $scene"}", "image/png", ".png",
                { (driver as TakesScreenshot).getScreenshotAs(OutputType.BYTES) })
    } catch (t: Throwable) {
        log.error("AllureProvider.kt: screenshot error! ",t)
    }
}

/**
 * 添加日志
 */
@Attachment(value = "TEST TXT LOG", type = "text/plain")
fun appendLogToAllure(file: File): ByteArray? {
    return try {
        FileUtils.readFileToByteArray(file)
    } catch (ignored: IOException) {
        null
    }
}
fun appendLog(name: String = "", file: File){
    try {
//        val fba = FileUtils.readFileToByteArray(file)
//        Allure.addByteAttachmentAsync("[${if (name == "") "TEST" else name}] LOG", "text/plain", ".log", { fba })
        val input: InputStream = FileInputStream(file)
        Allure.addAttachment("[${if (name == "") "TEST" else name}] LOG", "text/plain", input, ".log" )
    } catch (t: Throwable) {
        log.error("AllureProvider.kt: appendLog error! ",t)
    }
}