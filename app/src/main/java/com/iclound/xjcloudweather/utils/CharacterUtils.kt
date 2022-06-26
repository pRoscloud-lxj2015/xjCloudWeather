package com.iclound.xjcloudweather.utils

import com.iclound.xjcloudweather.widgets.bean.CharacterDiffResult


class CharacterUtils {
    companion object {
        fun diff(oldText: CharSequence, newText: CharSequence): List<CharacterDiffResult> {
            val differentList: MutableList<CharacterDiffResult> = ArrayList()
            val skip: MutableSet<Int> = HashSet()
            for (i in 0 until oldText.length) {
                val c = oldText[i]
                for (j in 0 until newText.length) {
                    if (!skip.contains(j) && c == newText[j]) {
                        skip.add(j)
                        val different = CharacterDiffResult(c, i, j)
                        differentList.add(different)
                        break
                    }
                }
            }
            return differentList
        }


        fun getOffset(from: Int, move: Int, progress: Float, startX: Float,
                      oldStartX: Float, gaps: Array<Float?>, oldGaps: Array<Float?>): Float {
            var dist = startX
            for (i in 0 until move) {
                dist += gaps[i]!!
            }
            var cur = oldStartX
            for (i in 0 until from) {
                cur += oldGaps[i]!!
            }
            return cur + (dist - cur) * progress
        }

        fun needMove(index: Int, differentList: List<CharacterDiffResult>): Int {
            differentList.forEach { item ->
                if(item.fromIndex == index){
                    return item.moveIndex
                }
            }
            return -1
        }

        fun stayHere(index: Int, differentList: List<CharacterDiffResult>): Boolean {
            differentList.forEach { item ->
                if(item.moveIndex == index){
                    return true
                }
            }
            return false
        }

    }

}