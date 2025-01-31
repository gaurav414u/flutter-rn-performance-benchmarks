package com.example.kmp_list_view_perf

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform