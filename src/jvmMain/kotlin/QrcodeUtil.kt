package com.ssk.ncmusic.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.util.*

/**
 * Created by ssk on 2023/2/8.
 */
object QrcodeUtil {

    /**
     * 创建二维码图片
     */
    fun createQrcodeFile(
        qrcodeStr: String,
        width: Int = 400,
        height: Int = 400,
    ): File? {
        // 用于设置QR二维码参数
        val qrParam = Hashtable<EncodeHintType, Any>()
        // 设置QR二维码的纠错级别——这里选择最高H级别
        qrParam[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        // 设置编码方式
        qrParam[EncodeHintType.CHARACTER_SET] = "UTF-8"

        try {
            val bitMatrix = MultiFormatWriter().encode(
                qrcodeStr,
                BarcodeFormat.QR_CODE, width, height, qrParam
            )
            val  file = File(System.getProperty("user.dir") + File.separator + "cache" + File.separator + "qrcode.png")
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            val path =  file.toPath()
            MatrixToImageWriter.writeToPath(bitMatrix, "png", path)
            return file
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}