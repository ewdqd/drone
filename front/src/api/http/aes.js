import CryptoJS from 'crypto-js'
import {CURRENT_CONFIG} from "@/api/http/config";


const AES_KEY =CURRENT_CONFIG.AES_KEY
// AES加密
export function encryptAES(data) {
    try {
        // 生成16字节随机IV
        const iv = CryptoJS.lib.WordArray.random(16)
        const key = CryptoJS.enc.Utf8.parse(AES_KEY)
        console.log('生成的IV(Hex):', iv.toString(CryptoJS.enc.Hex))


        const encrypted = CryptoJS.AES.encrypt(data, key, {
            iv:iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        })
        // 返回对象包含IV和加密数据
        return {
            iv: iv.toString(CryptoJS.enc.Hex),  // IV转为16进制字符串
            data: encrypted.toString()          // 加密数据
        }
     //   return encrypted.toString()
    } catch (error) {
        console.error('加密失败:', error)
        return null
    }
}
