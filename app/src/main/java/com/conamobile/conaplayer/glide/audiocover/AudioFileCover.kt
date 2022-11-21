package com.conamobile.conaplayer.glide.audiocover

class AudioFileCover(val filePath: String) {
    override fun hashCode(): Int {
        return filePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is AudioFileCover) {
            other.filePath == filePath
        } else false
    }
}