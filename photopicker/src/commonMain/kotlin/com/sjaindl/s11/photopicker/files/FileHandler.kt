package com.sjaindl.s11.photopicker.files

const val FILE_NAME = "profile_picture.png"

expect class FileHandler() {
    fun getTemporaryFilePath(byteArray: ByteArray): String?
}
