package yq.test.webSel3.handler

import org.testng.*
import org.testng.ITestResult
import org.testng.IInvokedMethod
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager.getRootLogger
import org.apache.log4j.PatternLayout
import yq.test.utils.Armory.getDateFormat
import java.io.File
import java.util.*


class AllureLogListener: ITestListener, IInvokedMethodListener {
    private var logFile: File? = null

    /**
     * 日志监听
     */
    private fun logAppender(result: ITestResult) {
        val apiLog = FileAppender()
        val time = Date().time
        val date = getDateFormat(time, "yyMMddHH", Locale.CHINA)
        val classSimpleName = result.testClass.name
        val methodName = result.method.methodName

        logFile = File("build/logs",
                "${date}_$classSimpleName.${methodName}_${UUID.randomUUID()}.htm")

        apiLog.name = "APILOG"
        apiLog.file = logFile!!.absolutePath
        apiLog.encoding = "UTF-8"

        val patternLayout = PatternLayout()
        patternLayout.conversionPattern = "%-d{yyyy-MM-dd HH:mm:ss} [%c]-[%p] - %m%n"
        apiLog.layout = patternLayout

        apiLog.immediateFlush = true
        apiLog.activateOptions()
        getRootLogger().addAppender(apiLog)
    }


    override fun beforeInvocation(method: IInvokedMethod, testResult: ITestResult) {
        logAppender(testResult)
    }

    override fun afterInvocation(method: IInvokedMethod, testResult: ITestResult) {
        appendLog(name = method.toString().limit(20), file = logFile!!)
    }

    //设置截图监听

    override fun onFinish(context: ITestContext?) {
    }

    override fun onTestSkipped(result: ITestResult?) {
        screenshot(scene = "test skipped")
    }

    override fun onTestSuccess(result: ITestResult?) {
    }

    override fun onTestFailure(result: ITestResult?) {
        screenshot(scene = "test failure")
    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult?) {
        screenshot(scene = "test failed but within success percentage")
    }

    override fun onTestStart(result: ITestResult?) {
    }

    override fun onStart(context: ITestContext?) {
    }
}