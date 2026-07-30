package com.example.util

/**
 * Utility function to convert Google Drive image links and standard URLs
 * into direct CDN image rendering URLs for Coil and Jetpack Compose.
 *
 * Supports:
 * - https://drive.google.com/file/d/FILE_ID/view?usp=sharing
 * - https://drive.google.com/open?id=FILE_ID
 * - https://drive.google.com/uc?id=FILE_ID
 * - Standard web image URLs (Unsplash, imgur, etc.)
 */
fun formatImageUrl(url: String?): String {
    if (url.isNullOrBlank()) return ""
    val trimmed = url.trim()

    if (trimmed.contains("drive.google.com") || trimmed.contains("docs.google.com")) {
        // Extract Google Drive File ID using regex pattern matching
        val fileIdRegex = Regex("(?:file/d/|id=|d/)([a-zA-Z0-9_-]{25,})")
        val match = fileIdRegex.find(trimmed)
        if (match != null) {
            val fileId = match.groupValues[1]
            return "https://lh3.googleusercontent.com/d/$fileId"
        }
    }
    return trimmed
}
